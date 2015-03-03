package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleCardDrawAction;

public class LootHoarder extends Minion {

    private static final boolean HERO_TARGETABLE = true;

    public LootHoarder() {
        super();
        heroTargetable_ = HERO_TARGETABLE;

        deathrattleAction_ = new DeathrattleCardDrawAction(1);
    }

}
