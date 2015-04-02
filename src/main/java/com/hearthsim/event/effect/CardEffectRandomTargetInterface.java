package com.hearthsim.event.effect;

import com.hearthsim.event.CharacterFilter;

public interface CardEffectRandomTargetInterface {
    public CardEffectCharacter getRandomTargetEffect();
    public CharacterFilter getRandomTargetFilter();
}
