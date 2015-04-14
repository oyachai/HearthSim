package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.event.CharacterFilter;

public interface CardEffectOnResolveRandomCharacterInterface extends CardEffectOnResolveRandomInterface<CardEffectCharacter<Card>, CharacterFilter> {
    @Override
    public CardEffectCharacter<Card> getRandomTargetEffect();

    @Override
    public CharacterFilter getRandomTargetFilter();
}
