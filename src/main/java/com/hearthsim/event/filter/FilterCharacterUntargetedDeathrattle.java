package com.hearthsim.event.filter;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public abstract class FilterCharacterUntargetedDeathrattle extends FilterCharacter {
    protected boolean includeDead() {
        return false;
    }

    @Override
    protected boolean excludeSource() {
        return true;
    }

    @Override
    public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
        if (!super.targetMatches(originSide, origin, targetSide, targetCharacter, board)) {
            return false;
        }

        if (!this.includeDead() && !targetCharacter.isAlive()) {
            return false;
        }

        return true;
    }
}
