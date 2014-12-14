package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Murloc;

public class MurlocRaider extends Murloc {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = (byte) 0;
	
	public MurlocRaider() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}
}
