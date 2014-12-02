package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.MinionWithEnrage;

public class GrommashHellscream extends MinionWithEnrage {

	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public GrommashHellscream() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        summoned_ = SUMMONED;
        transformed_ = TRANSFORMED;
	}

	
	public void enrage() {
		attack_ = (byte)(attack_ + 6);
	}

	
	public void pacify() {
		attack_ = (byte)(attack_ - 6);
	}
}
