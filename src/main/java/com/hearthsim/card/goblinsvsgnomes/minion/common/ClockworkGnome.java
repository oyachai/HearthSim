package com.hearthsim.card.goblinsvsgnomes.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattlePutSpareParts;

/**
 * Created by oyachai on 8/11/15.
 */
public class ClockworkGnome extends Minion {

    public ClockworkGnome() {
        super();
        deathrattleAction_ = new DeathrattlePutSpareParts();
    }

}
