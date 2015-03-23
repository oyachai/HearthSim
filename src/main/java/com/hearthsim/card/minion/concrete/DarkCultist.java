package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.EffectMinionAction;
import com.hearthsim.event.EffectMinionBuff;
import com.hearthsim.event.MinionFilterUntargetedDeathrattle;
import com.hearthsim.event.deathrattle.DeathrattleEffectRandomMinion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class DarkCultist extends Minion {

    private final static EffectMinionAction<Card> darkCultistEffect = new EffectMinionBuff<>(0, 3);

    private final static MinionFilterUntargetedDeathrattle filter = new MinionFilterUntargetedDeathrattle() {
        @Override
        protected boolean includeOwnMinions() { return true; }
    };

    public DarkCultist() {
        super();
        deathrattleAction_ = new DeathrattleEffectRandomMinion(DarkCultist.darkCultistEffect, DarkCultist.filter);
    }
}
