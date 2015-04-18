package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.MirrorImageMinion;
import com.hearthsim.card.minion.concrete.SpiritWolf;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterSummon;
import com.hearthsim.event.effect.EffectCharacterSummonNew;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.model.PlayerModel;

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
