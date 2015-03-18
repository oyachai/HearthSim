package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class PriestessOfElune extends Minion implements MinionUntargetableBattlecry {


    public PriestessOfElune() {
        super();
    }

    /**
     * Battlecry: Restore 4 Health to your Hero
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            int minionPlacementIndex,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) throws HSException {
        HearthTreeNode toRet = boardState;
        toRet = toRet.data_.getCurrentPlayer().getHero().takeHeal((byte)4, PlayerSide.CURRENT_PLAYER, toRet);
        return toRet;
    }

}
