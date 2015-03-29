package com.hearthsim.event;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class CharacterFilterSummon extends CharacterFilter {
    @Override
    public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
        if (!super.targetMatches(originSide, origin, targetSide, targetCharacter, board)) {
            return false;
        }

        if (board.modelForSide(targetSide).isBoardFull()) {
            return false;
        }

        return true;
    }

    public final static CharacterFilter SELF = new CharacterFilterSummon() {
        @Override
        protected boolean includeOwnHero() { return true; }
    };
}
