package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.EffectMinionAction;
import com.hearthsim.event.FilterUntargetedDeathrattle;
import com.hearthsim.event.EffectMinionBounce;
import com.hearthsim.event.deathrattle.DeathrattleEffectRandomMinion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class AnubarAmbusher extends Minion {

    private final static EffectMinionAction<Card> bounceEffect = new EffectMinionBounce();

    private final static FilterUntargetedDeathrattle filter = new FilterUntargetedDeathrattle() {
        @Override
        protected boolean canEffectOwnMinions() { return true; }
    };

    public AnubarAmbusher() {
        super();
        deathrattleAction_ = new DeathrattleEffectRandomMinion(AnubarAmbusher.bounceEffect, AnubarAmbusher.filter);
    }
}
