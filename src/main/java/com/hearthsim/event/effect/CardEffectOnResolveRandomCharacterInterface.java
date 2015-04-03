package com.hearthsim.event.effect;

import com.hearthsim.event.CharacterFilter;

public interface CardEffectOnResolveRandomCharacterInterface {
    public CardEffectCharacter getRandomTargetEffect();
    public CardEffectCharacter getRandomTargetSecondaryEffect();
    public CharacterFilter getRandomTargetFilter();
}
