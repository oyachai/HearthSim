package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleSummonMinionAction;

public class HarvestGolem extends Minion {

    private static final boolean HERO_TARGETABLE = true;

    public HarvestGolem() {
        super();
        heroTargetable_ = HERO_TARGETABLE;

        deathrattleAction_ = new DeathrattleSummonMinionAction(DamagedGolem.class, 1);
    }
}
