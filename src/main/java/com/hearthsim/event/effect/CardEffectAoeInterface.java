package com.hearthsim.event.effect;

import com.hearthsim.event.CharacterFilter;

public interface CardEffectAoeInterface {
    public CardEffectCharacter getAoeEffect();
    public CharacterFilter getAoeFilter();
}
