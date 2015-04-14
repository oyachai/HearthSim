package com.hearthsim.event;

import com.hearthsim.card.Card;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

@FunctionalInterface
public interface HandFilterInterface extends FilterInterface<Card, Card> {
    public default boolean targetMatches(PlayerSide originSide, int originIndex, PlayerSide targetSide, int targetCardIndex, BoardModel board) {
        Card origin = board.getCard_hand(originSide, originIndex);
        Card targetCard = board.getCard_hand(targetSide, targetCardIndex);
        return this.targetMatches(originSide, origin, targetSide, targetCard, board);
    }

    public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Card targetCard, BoardModel board);
}
