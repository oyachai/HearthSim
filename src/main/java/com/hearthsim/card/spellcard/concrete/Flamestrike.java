package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectOnResolveAoeInterface;

public class Flamestrike extends SpellDamage implements CardEffectOnResolveAoeInterface {

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public Flamestrike() {
        super();
    }

    @Override
    public CardEffectCharacter getAoeEffect() { return this.getSpellDamageEffect(); }

    @Override
    public CharacterFilter getAoeFilter() {
        return CharacterFilter.ENEMY_MINIONS;
    }
}
