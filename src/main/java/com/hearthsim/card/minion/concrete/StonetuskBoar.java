package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;

public class StonetuskBoar extends Minion {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public StonetuskBoar() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

        this.tribe = MinionTribe.BEAST;
	}

}
