package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Beast;
import com.hearthsim.event.deathrattle.DeathrattleSummonMinionAction;

public class SavannahHighmane extends Beast {

	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public SavannahHighmane() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        summoned_ = SUMMONED;
        transformed_ = TRANSFORMED;
        deathrattleAction_ = new DeathrattleSummonMinionAction(Hyena.class, 2);
	}

}
