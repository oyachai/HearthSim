package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Beast;

public class StonetuskBoar extends Beast {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public StonetuskBoar() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}

}
