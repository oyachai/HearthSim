package com.hearthsim.card.classic.minion.legendary;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleSummonMinionAction;

public class CairneBloodhoof extends Minion {

    public CairneBloodhoof() {
        super();

        deathrattleAction_ = new DeathrattleSummonMinionAction(BaineBloodhoof.class, 1);
    }
}
