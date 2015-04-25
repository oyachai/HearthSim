package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.event.filter.FilterHand;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public interface ActiveEffectHand extends ActiveEffectInterface<Card, SimpleEffectHand, FilterHand> {
    @Override
    public default boolean isActive(PlayerSide originSide, Card origin, BoardModel board) {
        return !origin.isInHand();
    }
}
