package com.hearthsim.event.battlecry;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public abstract class BattlecryActionTargetable extends BattlecryAction implements MinionTargetableBattlecry {
    protected boolean canTargetEnemyHero() { return false; }
    protected boolean canTargetEnemyMinions() { return false; }
    protected boolean canTargetOwnHero() { return false; }
    protected boolean canTargetOwnMinions() { return false; }

    protected Minion.MinionTribe tribeFilter() { return null; }

    public boolean canTargetWithBattlecry(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, BoardModel board) {
        Minion targetCharacter = board.modelForSide(targetSide).getCharacter(targetCharacterIndex);

        if (!this.canTargetOwnHero() && targetCharacter.isHero() && originSide == targetSide) {
            return false;
        }

        if (!this.canTargetEnemyHero() && targetCharacter.isHero() && originSide != targetSide) {
            return false;
        }

        if (!this.canTargetOwnMinions() && !targetCharacter.isHero() && originSide == targetSide) {
            return false;
        }

        if (!this.canTargetEnemyMinions() && !targetCharacter.isHero() && originSide != targetSide) {
            return false;
        }

        if (originSide != targetSide && targetCharacter.getStealthed()) {
            return false;
        }

        // cannot target self with battlecry
        if (origin == targetCharacter) {
            return false;
        }

        if (this.tribeFilter() != null && targetCharacter.getTribe() != this.tribeFilter()) {
            return false;
        }

        return true;
    }
}
