package com.hearthsim.card.classic.spell.rare;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuff;
import com.hearthsim.event.effect.EffectOnResolveAoe;
import com.hearthsim.event.filter.FilterCharacter;

public class Equality extends SpellCard implements EffectOnResolveAoe {

    private static final EffectCharacter effect = new EffectCharacterBuff(0, 1);

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public Equality(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Change the Health of ALL minions to 1
     */
    public Equality() {
        super();
    }

    @Override
    public EffectCharacter getAoeEffect() {
        return Equality.effect;
    }

    @Override
    public FilterCharacter getAoeFilter() {
        return FilterCharacter.ALL_MINIONS;
    }
}
