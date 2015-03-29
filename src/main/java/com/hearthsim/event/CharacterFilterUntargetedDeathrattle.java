package com.hearthsim.event;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public abstract class CharacterFilterUntargetedDeathrattle extends CharacterFilter {
    protected boolean includeDead() { return false; }
    protected boolean includeSelf() { return false; }

    @Override
    public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
        if (!super.targetMatches(originSide, origin, targetSide, targetCharacter, board)) {
            return false;
        }

        if (!this.includeSelf() && targetCharacter == origin) {
            return false;
        }

        if (!this.includeDead() && !targetCharacter.isAlive()) {
            return false;
        }

        return true;
    }
}
