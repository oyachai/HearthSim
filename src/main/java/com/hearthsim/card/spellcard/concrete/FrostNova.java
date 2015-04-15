package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectOnResolveAoe;

public class FrostNova extends SpellCard implements EffectOnResolveAoe {

    /**
     * This freeze all enemy minions
     */
    public FrostNova() {
        super();
    }

    @Override
    public EffectCharacter getAoeEffect() {
        return EffectCharacter.FREEZE;
    }

    @Override
    public FilterCharacter getAoeFilter() {
        return FilterCharacter.ENEMY_MINIONS;
    }
}
