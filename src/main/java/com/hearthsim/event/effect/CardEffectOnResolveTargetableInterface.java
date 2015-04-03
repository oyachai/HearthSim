package com.hearthsim.event.effect;

import com.hearthsim.event.CharacterFilter;

public interface CardEffectOnResolveTargetableInterface {
    public CardEffectCharacter getTargetableEffect();
    public CharacterFilter getTargetableFilter();
}
