package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleSummonMinionAction;

public class NerubianEgg extends Minion {

    private static final boolean HERO_TARGETABLE = true;

    public NerubianEgg() {
        super();
        heroTargetable_ = HERO_TARGETABLE;

        deathrattleAction_ = new DeathrattleSummonMinionAction(Nerubian.class, 1);
    }
}
