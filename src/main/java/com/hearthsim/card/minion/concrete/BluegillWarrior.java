package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Murloc;

public class BluegillWarrior extends Murloc {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public BluegillWarrior() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}
}
