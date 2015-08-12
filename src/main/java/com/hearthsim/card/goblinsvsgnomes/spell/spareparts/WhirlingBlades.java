package com.hearthsim.card.goblinsvsgnomes.spell.spareparts;

import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffDelta;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;

/**
 * Created by oyachai on 8/11/15.
 */
public class WhirlingBlades extends SpellTargetableCard {

    private final static EffectCharacter effect = new EffectCharacterBuffDelta(1, 0);

    public WhirlingBlades() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.ALL_MINIONS;
    }

    @Override
    public EffectCharacter getTargetableEffect() {
        return WhirlingBlades.effect;
    }
}

