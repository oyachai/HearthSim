package com.hearthsim.card.curseofnaxxramas.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleDamageAllMinions;

public class UnstableGhoul extends Minion {

    public UnstableGhoul() {
        super();

        deathrattleAction_ = new DeathrattleDamageAllMinions((byte)1);
    }
}
