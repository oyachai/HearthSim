package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class RazorfenHunter extends Minion implements MinionUntargetableBattlecry {

    public RazorfenHunter() {
        super();
    }

    /**
     * Battlecry: Summon a 1/1 Boar
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            int minionPlacementIndex,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) {
        HearthTreeNode toRet = boardState;
        PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        if (!currentPlayer.isBoardFull()) {
            Minion mdragon = new Boar();
            toRet = mdragon.summonMinion(PlayerSide.CURRENT_PLAYER, this, boardState, false, singleRealizationOnly);
        }
        return toRet;
    }

}
