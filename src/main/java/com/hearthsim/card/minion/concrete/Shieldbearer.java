package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;

public class Shieldbearer extends Minion {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public Shieldbearer() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}

}
