package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;

public class FaerieDragon extends Minion {

	private static final boolean HERO_TARGETABLE = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public FaerieDragon() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

        this.tribe = MinionTribe.DRAGON;
	}
}
