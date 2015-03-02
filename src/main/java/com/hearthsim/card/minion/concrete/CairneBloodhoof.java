package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleSummonMinionAction;

public class CairneBloodhoof extends Minion {

    private static final boolean HERO_TARGETABLE = true;

    public CairneBloodhoof() {
        super();
        heroTargetable_ = HERO_TARGETABLE;

        deathrattleAction_ = new DeathrattleSummonMinionAction(BaineBloodhoof.class, 1);
    }
}
