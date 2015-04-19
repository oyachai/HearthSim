package com.hearthsim.card.classic.spell.common;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterHeal;
import com.hearthsim.event.effect.EffectOnResolveAoe;
import com.hearthsim.event.filter.FilterCharacter;

public class CircleOfHealing extends SpellCard implements EffectOnResolveAoe {

    private static final byte HEAL_AMOUNT = 4;

    private static final EffectCharacter effect = new EffectCharacterHeal(CircleOfHealing.HEAL_AMOUNT);

    /**
     * Heals all minions for 4
     */
    public CircleOfHealing() {
        super();
    }

    @Override
    public EffectCharacter getAoeEffect() {
        return CircleOfHealing.effect;
    }

    @Override
    public FilterCharacter getAoeFilter() {
        return FilterCharacter.ALL_MINIONS;
    }
}
