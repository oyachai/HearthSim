package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectOnResolveAoe;

public class Hellfire extends SpellDamage implements EffectOnResolveAoe {

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public Hellfire(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public Hellfire() {
        super();
    }

    @Override
    public EffectCharacter getAoeEffect() {
        return this.getSpellDamageEffect();
    }

    @Override
    public FilterCharacter getAoeFilter() {
        return FilterCharacter.ALL;
    }
}
