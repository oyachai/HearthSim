package com.hearthsim.event;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public abstract class EffectMinionActionUntargetable extends EffectMinionAction<Card, BoardModel> {
    protected boolean canEffectDead() { return false; }
    protected boolean canEffectEnemyHero() { return false; }
    protected boolean canEffectEnemyMinions() { return false; }
    protected boolean canEffectOwnHero() { return false; }
    protected boolean canEffectOwnMinions() { return false; }
    protected boolean canEffectSelf() { return false; }
    protected Minion.MinionTribe tribeFilter() { return null; }

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

        if (this.tribeFilter() != null && targetCharacter.getTribe() != this.tribeFilter()) {
            return false;
        }

        return true;
    }

    public final void applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
        int index = board.modelForSide(targetSide).getIndexForCharacter(targetCharacter);
        this.applyEffect(originSide, origin, targetSide, index, board);
    }
}
