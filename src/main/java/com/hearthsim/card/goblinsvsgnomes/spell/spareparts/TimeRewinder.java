package com.hearthsim.card.goblinsvsgnomes.spell.spareparts;

import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;

/**
 * Created by oyachai on 8/11/15.
 */
public class TimeRewinder extends SpellTargetableCard {

    public TimeRewinder() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.FRIENDLY_MINIONS;
    }

    @Override
    public EffectCharacter getTargetableEffect() {
        return EffectCharacter.BOUNCE;
    }
}

