package com.hearthsim.card.classic.spell.common;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectOnResolveAoe;
import com.hearthsim.event.filter.FilterCharacter;

/**
 * Created by oyachai on 8/10/15.
 */
public class Conceal extends SpellCard implements EffectOnResolveAoe {

    public Conceal() {
        super();
    }

    @Override
    public EffectCharacter getAoeEffect() {
        return EffectCharacter.STEALTH_UNTIL_NEXT_TURN;
    }

    @Override
    public FilterCharacter getAoeFilter() {
        return FilterCharacter.FRIENDLY_MINIONS;
    }
}
