package com.hearthsim.event.effect;

import com.hearthsim.event.CharacterFilter;

public interface CardEffectOnResolveRandomCharacterInterface {
    public CardEffectCharacter getRandomTargetEffect();
    public CharacterFilter getRandomTargetFilter();
}
