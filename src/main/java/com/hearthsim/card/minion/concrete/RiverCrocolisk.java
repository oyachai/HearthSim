package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Beast;

public class RiverCrocolisk extends Beast {

	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public RiverCrocolisk() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        summoned_ = SUMMONED;
        transformed_ = TRANSFORMED;
	}

}
