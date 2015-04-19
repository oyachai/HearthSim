package com.hearthsim.card.curseofnaxxramas.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleSummonMinionAction;

public class HauntedCreeper extends Minion {

    public HauntedCreeper() {
        super();

        deathrattleAction_ = new DeathrattleSummonMinionAction(SpectralSpider.class, 2);
    }

}
