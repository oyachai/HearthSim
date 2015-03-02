package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleSummonMinionAction;

public class SludgeBelcher extends Minion {

    private static final boolean HERO_TARGETABLE = true;

    public SludgeBelcher() {
        super();
        heroTargetable_ = HERO_TARGETABLE;

        deathrattleAction_ = new DeathrattleSummonMinionAction(Slime.class, 1);
    }
}
