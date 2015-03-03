package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleDamageAll;

public class Abomination extends Minion {

    private static final boolean HERO_TARGETABLE = true;

    public Abomination() {
        super();
        heroTargetable_ = HERO_TARGETABLE;
        deathrattleAction_ = new DeathrattleDamageAll((byte) 2);
    }

}
