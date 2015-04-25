package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.event.filter.FilterInterface;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public interface ActiveEffectInterface<T extends Card, U extends EffectInterface<T>, V extends FilterInterface<T>> {
    public boolean isActive(PlayerSide originSide, T origin, BoardModel board);

    public U getActiveEffect();

    public U undoActiveEffect();

    public V getActiveFilter();
}
