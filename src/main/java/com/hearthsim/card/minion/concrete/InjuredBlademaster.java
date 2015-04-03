package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class InjuredBlademaster extends Minion implements MinionUntargetableBattlecry {

    public InjuredBlademaster() {
        super();
    }

    /**
     * Battlecry: Deal 4 damage to himself
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            int minionPlacementIndex,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) {
        HearthTreeNode toRet;
        toRet = this.takeDamageAndNotify((byte) 4, PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, boardState, false, true);
        return toRet;
    }

}
