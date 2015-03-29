package com.hearthsim.event.effect;

import com.hearthsim.event.MinionFilter;

public interface CardEffectAoeInterface {
    public CardEffectCharacter getAoeEffect();
    public MinionFilter getAoeFilter();
}
