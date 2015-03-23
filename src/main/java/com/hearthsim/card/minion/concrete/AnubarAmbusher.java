package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.EffectMinionAction;
import com.hearthsim.event.MinionFilterUntargetedDeathrattle;
import com.hearthsim.event.EffectMinionBounce;
import com.hearthsim.event.deathrattle.DeathrattleEffectRandomMinion;

public class AnubarAmbusher extends Minion {

    private final static EffectMinionAction<Card> bounceEffect = new EffectMinionBounce();

    private final static MinionFilterUntargetedDeathrattle filter = new MinionFilterUntargetedDeathrattle() {
        @Override
        protected boolean includeOwnMinions() { return true; }
    };

    public AnubarAmbusher() {
        super();
        deathrattleAction_ = new DeathrattleEffectRandomMinion(AnubarAmbusher.bounceEffect, AnubarAmbusher.filter);
    }
}
