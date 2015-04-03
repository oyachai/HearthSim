package com.hearthsim.event.effect;

import com.hearthsim.event.CharacterFilter;

public interface CardEffectOnResolveAoeInterface {
    public CardEffectCharacter getAoeEffect();
    public CharacterFilter getAoeFilter();
}
