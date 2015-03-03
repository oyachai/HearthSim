package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleCardDrawAction;

public class LootHoarder extends Minion {

    public LootHoarder() {
        super();

        deathrattleAction_ = new DeathrattleCardDrawAction(1);
    }

}
