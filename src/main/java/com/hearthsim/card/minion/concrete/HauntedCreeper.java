package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleSummonMinionAction;

public class HauntedCreeper extends Minion {

    private static final boolean HERO_TARGETABLE = true;

    public HauntedCreeper() {
        super();
        heroTargetable_ = HERO_TARGETABLE;

        deathrattleAction_ = new DeathrattleSummonMinionAction(SpectralSpider.class, 2);
    }

}
