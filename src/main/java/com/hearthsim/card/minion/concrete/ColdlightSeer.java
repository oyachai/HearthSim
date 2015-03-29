package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class ColdlightSeer extends Minion implements MinionUntargetableBattlecry {

    public ColdlightSeer() {
        super();
    }

    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            int minionPlacementIndex,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) {
        HearthTreeNode toRet = boardState;
        PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        for (Minion minion : currentPlayer.getMinions()) {
            if (minion != this && minion.getTribe() == MinionTribe.MURLOC) {
                minion.addHealth((byte)2);
                minion.addMaxHealth((byte)2);
            }
        }
        return toRet;
    }
}
