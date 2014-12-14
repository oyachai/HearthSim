package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleCardDrawAction;

public class BloodmageThalnos extends Minion {

	private static final boolean HERO_TARGETABLE = true;
	
	private static final byte SPELL_DAMAGE = 1;
	
	public BloodmageThalnos() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

        deathrattleAction_= new DeathrattleCardDrawAction(1);
	}
	
}
