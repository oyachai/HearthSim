package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Beast;

public class Huffer extends Beast {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public Huffer() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}

}
