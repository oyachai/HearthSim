package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleMindControl;

/**
 * Created by oyachai on 1/12/15.
 */
public class SylvanasWindrunner extends Minion {
    private static final boolean HERO_TARGETABLE = true;

    public SylvanasWindrunner() {
        super();
        heroTargetable_ = HERO_TARGETABLE;
        deathrattleAction_ = new DeathrattleMindControl();
    }

}
