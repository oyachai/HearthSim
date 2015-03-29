package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class GuardianOfKings extends Minion implements MinionUntargetableBattlecry {

    public GuardianOfKings() {
        super();
    }

    /**
     * Battlecry: Restore 6 Health to your Hero
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            int minionPlacementIndex,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) {
        HearthTreeNode toRet = boardState;
        toRet = toRet.data_.getCurrentPlayer().getHero().takeHealAndNotify((byte) 6, PlayerSide.CURRENT_PLAYER, toRet);
        return toRet;
    }

}
