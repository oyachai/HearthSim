package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardInHandIndex;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

@FunctionalInterface
public interface EffectHand {

    public default HearthTreeNode applyEffect(PlayerSide originSide, CardInHandIndex originIndex, PlayerSide targetSide, CardInHandIndex targetCardIndex, HearthTreeNode boardState) {
        Card origin = boardState.data_.getCard_hand(originSide, originIndex);
        Card targetCard = boardState.data_.getCard_hand(targetSide, targetCardIndex);
        return this.applyEffect(originSide, origin, targetSide, targetCard, boardState);
    }

    public default HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, CardInHandIndex targetCardIndex, HearthTreeNode boardState) {
        Card targetCard = boardState.data_.getCard_hand(targetSide, targetCardIndex);
        return this.applyEffect(originSide, origin, targetSide, targetCard, boardState);
    }

    HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, Card targetCard, HearthTreeNode boardState);
}
