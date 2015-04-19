package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleDamageAll;

public class Abomination extends Minion {

    public Abomination() {
        super();
        deathrattleAction_ = new DeathrattleDamageAll((byte) 2);
    }

}
