package com.hearthsim.card.curseofnaxxramas.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleCardDrawAction;


public class DancingSwords extends Minion {

    public DancingSwords() {
        super();
        deathrattleAction_ = new DeathrattleCardDrawAction(1, true);
    }

}
