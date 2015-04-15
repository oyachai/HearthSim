package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectOnResolveAoe;

public class TwistingNether extends SpellCard implements EffectOnResolveAoe {

    public TwistingNether() {
        super();
    }

    @Override
    public EffectCharacter getAoeEffect() {
        return EffectCharacter.DESTROY;
    }

    @Override
    public FilterCharacter getAoeFilter() {
        return FilterCharacter.ALL_MINIONS;
    }
}
