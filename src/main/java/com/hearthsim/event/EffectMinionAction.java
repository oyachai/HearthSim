package com.hearthsim.event;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public abstract class EffectMinionAction {
    protected boolean canEffectDead() { return false; }
    protected boolean canEffectEnemyHero() { return false; }
    protected boolean canEffectEnemyMinions() { return false; }
    protected boolean canEffectOwnHero() { return false; }
    protected boolean canEffectOwnMinions() { return false; }
    protected boolean canEffectSelf() { return false; }

    public boolean canEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
        if (!this.canEffectOwnHero() && targetCharacter.isHero() && originSide == targetSide) {
            return false;
        }

        if (!this.canEffectEnemyHero() && targetCharacter.isHero() && originSide != targetSide) {
            return false;
        }

        if (!this.canEffectOwnMinions() && !targetCharacter.isHero() && originSide == targetSide) {
            return false;
        }

        if (!this.canEffectEnemyMinions() && !targetCharacter.isHero() && originSide != targetSide) {
            return false;
        }

        if (!this.canEffectSelf() && targetCharacter == origin) {
            return false;
        }

        if (!this.canEffectDead() && !targetCharacter.isAlive()) {
            return false;
        }

        return true;
    }

    public abstract void applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, BoardModel board);

    public final void applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
        int index = board.modelForSide(targetSide).getIndexForCharacter(targetCharacter);
        this.applyEffect(originSide, origin, targetSide, index, board);
    }
}
