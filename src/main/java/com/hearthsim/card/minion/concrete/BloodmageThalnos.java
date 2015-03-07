package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleCardDrawAction;

public class BloodmageThalnos extends Minion {

    public BloodmageThalnos() {
        super();

        deathrattleAction_= new DeathrattleCardDrawAction(1);
    }
}
