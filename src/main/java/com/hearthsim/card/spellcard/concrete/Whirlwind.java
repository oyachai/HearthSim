package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectOnResolveAoe;

public class Whirlwind extends SpellDamage implements EffectOnResolveAoe {

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public Whirlwind() {
        super();
    }

    @Override
    public EffectCharacter getAoeEffect() {
        return this.getSpellDamageEffect();
    }

    @Override
    public FilterCharacter getAoeFilter() {
        return FilterCharacter.ALL_MINIONS;
    }
}
