package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleHealHeroAction;

public class ZombieChow extends Minion {

    public ZombieChow() {
        super();

        deathrattleAction_ = new DeathrattleHealHeroAction((byte)5, true);
    }
}
