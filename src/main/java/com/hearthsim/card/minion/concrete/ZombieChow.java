package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleHealHeroAction;

public class ZombieChow extends Minion {

    private static final boolean HERO_TARGETABLE = true;

    public ZombieChow() {
        super();
        heroTargetable_ = HERO_TARGETABLE;

        deathrattleAction_ = new DeathrattleHealHeroAction((byte)5, true);
    }
}
