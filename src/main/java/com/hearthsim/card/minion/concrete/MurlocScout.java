package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Murloc;

public class MurlocScout extends Murloc {

	private static final boolean HERO_TARGETABLE = true;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public MurlocScout() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        transformed_ = TRANSFORMED;
	}
}
