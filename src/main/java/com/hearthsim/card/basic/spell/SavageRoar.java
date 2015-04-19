package com.hearthsim.card.basic.spell;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffTemp;
import com.hearthsim.event.effect.EffectOnResolveAoe;
import com.hearthsim.event.filter.FilterCharacter;

public class SavageRoar extends SpellCard implements EffectOnResolveAoe {

    private final static EffectCharacter effect = new EffectCharacterBuffTemp(2);

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public SavageRoar(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Gives all friendly characters +2 attack for this turn
     */
    public SavageRoar() {
        super();
    }

    @Override
    public EffectCharacter getAoeEffect() {
        return SavageRoar.effect;
    }

    @Override
    public FilterCharacter getAoeFilter() {
        return FilterCharacter.ALL_FRIENDLIES;
    }
}
