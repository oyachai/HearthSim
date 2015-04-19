package com.hearthsim.card.classic.spell.rare;

import com.hearthsim.card.classic.minion.rare.SpiritWolf;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterSummonNew;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;

public class FeralSpirit extends SpellTargetableCard {

    private static final EffectCharacter effect = new EffectCharacterSummonNew<>(SpiritWolf.class, 2, true);

    public FeralSpirit() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.SELF;
    }

    @Override
    public EffectCharacter getTargetableEffect() {
        return FeralSpirit.effect;
    }
}
