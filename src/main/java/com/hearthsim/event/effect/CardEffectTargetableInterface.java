package com.hearthsim.event.effect;

import com.hearthsim.event.CharacterFilter;

public interface CardEffectTargetableInterface {
    public CardEffectCharacter getTargetableEffect();
    public CharacterFilter getTargetableFilter();
}
