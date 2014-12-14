package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;

public class OgreMagi extends Minion {

	private static final boolean HERO_TARGETABLE = true;
	
	private static final byte SPELL_DAMAGE = 1;
	
	public OgreMagi() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}
}
