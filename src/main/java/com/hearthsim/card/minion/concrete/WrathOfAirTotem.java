package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Totem;

public class WrathOfAirTotem extends Totem {

	private static final boolean HERO_TARGETABLE = true;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 1;

    public WrathOfAirTotem() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        transformed_ = TRANSFORMED;
    }

}
