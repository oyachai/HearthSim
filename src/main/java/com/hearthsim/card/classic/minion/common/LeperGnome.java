package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleDealDamageEnemyHeroAction;

public class LeperGnome extends Minion {

    public LeperGnome() {
        super();

        deathrattleAction_ = new DeathrattleDealDamageEnemyHeroAction((byte)2);
    }
}
