package com.hearthsim.card.goblinsvsgnomes.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleDamageAllMinions;

public class ExplosiveSheep extends Minion {

    public ExplosiveSheep() {
        super();
        deathrattleAction_ = new DeathrattleDamageAllMinions((byte) 2);
    }
}
