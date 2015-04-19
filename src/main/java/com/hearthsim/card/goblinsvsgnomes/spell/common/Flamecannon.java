package com.hearthsim.card.goblinsvsgnomes.spell.common;

import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectOnResolveRandomCharacter;
import com.hearthsim.event.filter.FilterCharacter;

public class Flamecannon extends SpellDamage implements EffectOnResolveRandomCharacter {
    @Override
    public EffectCharacter getRandomTargetEffect() {
        return this.getSpellDamageEffect();
    }

    @Override
    public FilterCharacter getRandomTargetFilter() {
        return FilterCharacter.ENEMY_MINIONS;
    }
}
