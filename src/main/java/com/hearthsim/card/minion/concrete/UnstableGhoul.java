package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleDamageAllMinions;

public class UnstableGhoul extends Minion {

    private static final boolean HERO_TARGETABLE = true;

    public UnstableGhoul() {
        super();
        heroTargetable_ = HERO_TARGETABLE;

        deathrattleAction_ = new DeathrattleDamageAllMinions((byte)1);
    }
}
