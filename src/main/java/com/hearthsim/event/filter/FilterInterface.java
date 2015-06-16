package com.hearthsim.event.filter;

import com.hearthsim.card.Card;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

@FunctionalInterface
public interface FilterInterface<T extends Card, V> {
    public boolean targetMatches(PlayerSide originSide, T origin, PlayerSide targetSide, V targetCardIndex, BoardModel board);
}
