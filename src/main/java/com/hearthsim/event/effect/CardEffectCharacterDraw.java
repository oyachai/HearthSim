package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class CardEffectCharacterDraw extends CardEffectCharacter {
    private final int amount;

    public CardEffectCharacterDraw(int amount) {
        this.amount = amount;
    }

    public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
        if (targetSide == PlayerSide.CURRENT_PLAYER) {
             if (boardState instanceof CardDrawNode) {
                 ((CardDrawNode) boardState).addNumCardsToDraw(this.amount);
             } else {
                 boardState = new CardDrawNode(boardState, this.amount);
             }
        } else {
            boardState.data_.drawCardFromWaitingPlayerDeck(1);
        }
        return boardState;
    }
}
