package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleCardDrawAction;

public class BloodmageThalnos extends Minion {

    private static final boolean HERO_TARGETABLE = true;

    public BloodmageThalnos() {
        super();
        heroTargetable_ = HERO_TARGETABLE;

        deathrattleAction_= new DeathrattleCardDrawAction(1);
    }
}
