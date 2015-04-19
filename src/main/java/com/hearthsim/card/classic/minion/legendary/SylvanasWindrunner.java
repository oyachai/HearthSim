package com.hearthsim.card.classic.minion.legendary;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleMindControl;

/**
 * Created by oyachai on 1/12/15.
 */
public class SylvanasWindrunner extends Minion {

    public SylvanasWindrunner() {
        super();
        deathrattleAction_ = new DeathrattleMindControl();
    }

}
