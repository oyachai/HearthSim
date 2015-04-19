package com.hearthsim.card.curseofnaxxramas.minion.rare;

import com.hearthsim.card.curseofnaxxramas.minion.common.Slime;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleSummonMinionAction;

public class SludgeBelcher extends Minion {

    public SludgeBelcher() {
        super();

        deathrattleAction_ = new DeathrattleSummonMinionAction(Slime.class, 1);
    }
}
