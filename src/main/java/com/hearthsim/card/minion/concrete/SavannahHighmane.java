package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleSummonMinionAction;

public class SavannahHighmane extends Minion {

    private static final boolean HERO_TARGETABLE = true;

    public SavannahHighmane() {
        super();
        heroTargetable_ = HERO_TARGETABLE;

        deathrattleAction_ = new DeathrattleSummonMinionAction(Hyena.class, 2);
    }

}
