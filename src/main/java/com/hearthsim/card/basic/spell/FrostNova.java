package com.hearthsim.card.basic.spell;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectOnResolveAoe;
import com.hearthsim.event.filter.FilterCharacter;

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
