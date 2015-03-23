package com.hearthsim.event;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class MinionFilter {
    protected boolean includeEnemyHero() {
        return false;
    }

    protected boolean includeEnemyMinions() {
        return false;
    }

    protected boolean includeOwnHero() {
        return false;
    }

    protected boolean includeOwnMinions() {
        return false;
    }

    protected Minion.MinionTribe tribeFilter() {
        return null;
    }

    public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
        if (!this.includeOwnHero() && targetCharacter.isHero() && originSide == targetSide) {
            return false;
        }

        if (!this.includeEnemyHero() && targetCharacter.isHero() && originSide != targetSide) {
            return false;
        }

        if (!this.includeOwnMinions() && !targetCharacter.isHero() && originSide == targetSide) {
            return false;
        }

        if (!this.includeEnemyMinions() && !targetCharacter.isHero() && originSide != targetSide) {
            return false;
        }

        if (this.tribeFilter() != null && targetCharacter.getTribe() != this.tribeFilter()) {
            return false;
        }

        return true;
    }

    public final boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, BoardModel board) {
        Minion targetCharacter = board.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        return this.targetMatches(originSide, origin, targetSide, targetCharacter, board);
    }
}
