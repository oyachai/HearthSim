package com.hearthsim.card.curseofnaxxramas.minion.rare;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleSummonMinionAction;

public class NerubianEgg extends Minion {

    public NerubianEgg() {
        super();

        deathrattleAction_ = new DeathrattleSummonMinionAction(Nerubian.class, 1);
    }
}
