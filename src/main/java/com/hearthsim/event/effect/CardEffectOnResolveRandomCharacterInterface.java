package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.event.filter.FilterCharacter;

public interface CardEffectOnResolveRandomCharacterInterface extends CardEffectOnResolveRandomInterface<CardEffectCharacter<Card>, FilterCharacter> {
    @Override
    public CardEffectCharacter<Card> getRandomTargetEffect();

    @Override
    public FilterCharacter getRandomTargetFilter();
}
