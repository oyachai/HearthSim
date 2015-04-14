package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

@FunctionalInterface
public interface CardEffectHand extends CardEffectInterface<Card, Card> {
    public default HearthTreeNode applyEffect(PlayerSide originSide, int originIndex, PlayerSide targetSide, int targetCardIndex, HearthTreeNode boardState) {
        Card origin = boardState.data_.getCard_hand(originSide, originIndex);
        Card targetCard = boardState.data_.getCard_hand(targetSide, targetCardIndex);
        return this.applyEffect(originSide, origin, targetSide, targetCard, boardState);
    }

    public default HearthTreeNode applyEffect(PlayerSide originSide, int originIndex, PlayerSide targetSide, Minion targetCard, HearthTreeNode boardState) {
        Card origin = boardState.data_.getCard_hand(originSide, originIndex);
        return this.applyEffect(originSide, origin, targetSide, targetCard, boardState);
    }

    @Override
    public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, Card target, HearthTreeNode boardState);
}
