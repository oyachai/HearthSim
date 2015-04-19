package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.concrete.TreantWithCharge;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterSummonNew;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;

public class ForceOfNature extends SpellTargetableCard {

    private static final EffectCharacter effect = new EffectCharacterSummonNew<>(TreantWithCharge.class, 3, true);

    public ForceOfNature() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.SELF;
    }

    @Override
    public EffectCharacter getTargetableEffect() {
        return ForceOfNature.effect;
    }
}
