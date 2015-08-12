package com.hearthsim.card.goblinsvsgnomes.spell.spareparts;

import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffDelta;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;

public class ArmorPlating extends SpellTargetableCard {

    private final static EffectCharacter effect = new EffectCharacterBuffDelta(0, 1);

    public ArmorPlating() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.ALL_MINIONS;
    }

    @Override
    public EffectCharacter getTargetableEffect() {
        return ArmorPlating.effect;
    }
}
