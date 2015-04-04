package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectOnResolveAoeInterface;

public class Whirlwind extends SpellDamage implements CardEffectOnResolveAoeInterface {

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public Whirlwind() {
        super();
    }

    @Override
    public CardEffectCharacter getAoeEffect() { return this.getSpellDamageEffect(); }

    @Override
    public CharacterFilter getAoeFilter() {
        return CharacterFilter.ALL_MINIONS;
    }
}
