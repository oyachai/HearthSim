package com.hearthsim.card.goblinsvsgnomes.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Snowchugger extends Minion {

    public Snowchugger() {
        super();
    }

    @Override
    protected HearthTreeNode attack_core(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode boardState) throws HSException {
        HearthTreeNode toRet = super.attack_core(targetMinionPlayerSide, targetMinion, boardState);
        if (!silenced_ && toRet != null) {
            targetMinion.setFrozen(true);
        }
        return toRet;
    }
}
