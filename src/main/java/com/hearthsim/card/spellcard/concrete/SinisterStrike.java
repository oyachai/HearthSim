package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamageTargetableCard;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;

public class SinisterStrike extends SpellDamageTargetableCard {

    public SinisterStrike() {
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.OPPONENT;
    }

}
