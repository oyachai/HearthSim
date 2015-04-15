package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.event.filter.FilterCharacter;

public interface EffectOnResolveAoe<T extends Card> {
    public EffectCharacter<T> getAoeEffect();
    public FilterCharacter getAoeFilter();
}
