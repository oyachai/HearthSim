package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.EffectMinionBounce;
import com.hearthsim.event.deathrattle.DeathrattleEffectRandomMinion;

public class AnubarAmbusher extends Minion {

    public AnubarAmbusher() {
        super();
        deathrattleAction_ = new DeathrattleEffectRandomMinion(new EffectMinionBounce());
    }
}
