package com.hearthsim.card.basic.spell;

import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectOnResolveAoe;
import com.hearthsim.event.filter.FilterCharacter;

public class Flamestrike extends SpellDamage implements EffectOnResolveAoe {

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public Flamestrike() {
        super();
    }

    @Override
    public EffectCharacter getAoeEffect() {
        return this.getSpellDamageEffect();
    }

    @Override
    public FilterCharacter getAoeFilter() {
        return FilterCharacter.ENEMY_MINIONS;
    }
}
