package com.hearthsim.event;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class EffectMinionBounce extends EffectMinionAction<Card> {
    public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
        Minion targetCharacter = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        if (boardState.data_.modelForSide(targetSide).getHand().size() < 10) {
            Minion copy = targetCharacter.createResetCopy();
            boardState.data_.modelForSide(targetSide).placeCardHand(copy);
        }
        boardState.data_.removeMinion(targetCharacter);
        return boardState;
    }
}
