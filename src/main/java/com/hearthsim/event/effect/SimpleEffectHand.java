package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardInHandIndex;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

@FunctionalInterface
public interface SimpleEffectHand extends SimpleEffectInterface<Card, CardInHandIndex> {
    public default void applyEffect(PlayerSide originSide, CardInHandIndex originIndex, PlayerSide targetSide, CardInHandIndex targetCardIndex, BoardModel board) {
        Card origin = board.getCard_hand(originSide, originIndex);
        Card targetCard = board.getCard_hand(targetSide, targetCardIndex);
        this.applyEffect(originSide, origin, targetSide, targetCard, board);
    }

    @Override
    public default void applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, CardInHandIndex targetCardIndex, BoardModel board) {
        Card targetCard = board.getCard_hand(targetSide, targetCardIndex);
        this.applyEffect(originSide, origin, targetSide, targetCard, board);
    }

    public void applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, Card targetCard, BoardModel board);
}
