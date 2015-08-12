package com.hearthsim.card.goblinsvsgnomes.spell.spareparts;

import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;

/**
 * Created by oyachai on 8/11/15.
 */
public class ReversingSwitch extends SpellTargetableCard {

    public ReversingSwitch() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.ALL_MINIONS;
    }

    @Override
    public EffectCharacter getTargetableEffect() {
        return EffectCharacter.SWAP_ATTACK_HEALTH;
    }
}


