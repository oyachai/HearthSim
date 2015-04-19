package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleSummonMinionAction;

public class SavannahHighmane extends Minion {

    public SavannahHighmane() {
        super();

        deathrattleAction_ = new DeathrattleSummonMinionAction(Hyena.class, 2);
    }

}
