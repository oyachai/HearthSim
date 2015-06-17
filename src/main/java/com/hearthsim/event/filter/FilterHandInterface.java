package com.hearthsim.event.filter;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardInHandIndex;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

@FunctionalInterface
public interface FilterHandInterface extends FilterInterface<Card, CardInHandIndex> {
    @Override
    public default boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, CardInHandIndex targetCardIndex, BoardModel board) {
        Card targetCard = board.getCard_hand(targetSide, targetCardIndex);
        return this.targetMatches(originSide, origin, targetSide, targetCard, board);
    }

    public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Card targetCard, BoardModel board);
}
