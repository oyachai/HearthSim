package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class SilverHandKnight extends Minion implements MinionUntargetableBattlecry {

    public SilverHandKnight() {
        super();
    }

    /**
     * Battlecry: Summon a Squire
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            int minionPlacementIndex,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) {
        HearthTreeNode toRet = boardState;
        if (toRet != null) {
            PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
            if (!currentPlayer.isBoardFull()) {
                Minion mdragon = new Squire();
                toRet = mdragon.summonMinion(PlayerSide.CURRENT_PLAYER, this, boardState, false, singleRealizationOnly);
            }
        }
        return toRet;
    }

}
