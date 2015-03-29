package com.hearthsim.event;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public abstract class CharacterFilterTargetedBattlecry extends CharacterFilter {
    @Override
    public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
        if (!super.targetMatches(originSide, origin, targetSide, targetCharacter, board)) {
            return false;
        }

        if (originSide != targetSide && targetCharacter.getStealthed()) {
            return false;
        }

        // cannot target self with battlecry
        if (origin == targetCharacter) {
            return false;
        }

        return true;
    }
}
