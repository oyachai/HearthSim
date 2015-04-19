package com.hearthsim.card.goblinsvsgnomes.weapon.common;

import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffDelta;
import com.hearthsim.event.effect.EffectOnResolveRandomCharacter;
import com.hearthsim.event.filter.FilterCharacter;

public class Glaivezooka extends WeaponCard implements EffectOnResolveRandomCharacter {
    private static final EffectCharacter effect = new EffectCharacterBuffDelta(1, 0);

    @Override
    public EffectCharacter getRandomTargetEffect() {
        return Glaivezooka.effect;
    }

    @Override
    public FilterCharacter getRandomTargetFilter() {
        return FilterCharacter.FRIENDLY_MINIONS;
    }
}
