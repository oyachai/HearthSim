package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.event.filter.FilterCharacter;

public interface CardEffectOnResolveAoeInterface<T extends Card> {
    public CardEffectCharacter<T> getAoeEffect();
    public FilterCharacter getAoeFilter();
}
