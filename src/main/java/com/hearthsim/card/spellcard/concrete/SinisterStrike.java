package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamageTargetableCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;

public class SinisterStrike extends SpellDamageTargetableCard {

    public SinisterStrike() {
    }

    @Override
    public CharacterFilter getTargetableFilter() {
        return CharacterFilterTargetedSpell.OPPONENT;
    }

}
