package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Totem;

public class StoneclawTotem extends Totem {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public StoneclawTotem() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}
	
}
