package com.hearthsim.card.basic.spell;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffTemp;
import com.hearthsim.event.effect.EffectOnResolveAoe;
import com.hearthsim.event.filter.FilterCharacter;

public class Bloodlust extends SpellCard implements EffectOnResolveAoe {

    private final static EffectCharacter effect = new EffectCharacterBuffTemp(3);

    /**
     * Give your minions +3 attack for this turn
     */
    public Bloodlust() {
        super();
    }

    @Deprecated
    public Bloodlust(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    @Override
    public EffectCharacter getAoeEffect() {
        return Bloodlust.effect;
    }

    @Override
    public FilterCharacter getAoeFilter() {
        return FilterCharacter.FRIENDLY_MINIONS;
    }
}
