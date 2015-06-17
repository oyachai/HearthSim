package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.event.filter.FilterHand;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public interface ActiveEffectHand {

    public SimpleEffectHand getActiveEffect();

    public SimpleEffectHand undoActiveEffect();

    public FilterHand getActiveFilter();

    public default boolean isActive(PlayerSide originSide, Card origin, BoardModel board) {
        return !origin.setInHand();
    }
}
