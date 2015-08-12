package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class EffectHeroDraw<T extends Card> implements EffectHero<T> {
    private final int amount;

    public EffectHeroDraw(int amount) {
        this.amount = amount;
    }

    @Override
    public HearthTreeNode applyEffect(PlayerSide targetSide, HearthTreeNode boardState) {
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
