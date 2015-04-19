package com.hearthsim.card.classic.spell.rare;

import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectHeroDraw;
import com.hearthsim.event.effect.EffectOnResolveAoe;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;

public class MassDispel extends SpellTargetableCard implements EffectOnResolveAoe {

    private static final EffectCharacter effect = new EffectHeroDraw(1);

    public MassDispel() {
        super();
    }

    @Override
    public EffectCharacter getAoeEffect() {
        return EffectCharacter.SILENCE;
    }

    @Override
    public FilterCharacter getAoeFilter() {
        return FilterCharacter.ENEMY_MINIONS;
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.SELF;
    }

    @Override
    public EffectCharacter getTargetableEffect() {
        return effect;
    }
}
