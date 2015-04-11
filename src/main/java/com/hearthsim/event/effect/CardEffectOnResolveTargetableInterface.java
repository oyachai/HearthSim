package com.hearthsim.event.effect;

import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;

public interface CardEffectOnResolveTargetableInterface {
    public CardEffectCharacter getTargetableEffect();
    public default CharacterFilter getTargetableFilter() {
        return CharacterFilterTargetedSpell.ALL;
    }
}
