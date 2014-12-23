package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleSummonMinionAction;

public class HauntedCreeper extends Minion {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public HauntedCreeper() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

        deathrattleAction_ = new DeathrattleSummonMinionAction(SpectralSpider.class, 2);

        this.tribe = MinionTribe.BEAST;
	}

}
