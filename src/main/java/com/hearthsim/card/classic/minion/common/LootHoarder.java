package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleCardDrawAction;

public class LootHoarder extends Minion {

    public LootHoarder() {
        super();

        deathrattleAction_ = new DeathrattleCardDrawAction(1);
    }

}
