package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class MurlocTidehunter extends Minion implements MinionUntargetableBattlecry {

    public MurlocTidehunter() {
        super();
    }

    /**
     * Battlecry: Summon a Murloc Scout
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
                Minion mdragon = new MurlocScout();
                toRet = mdragon.summonMinion(PlayerSide.CURRENT_PLAYER, this, boardState, false, singleRealizationOnly);
            }
        }
        return toRet;
    }

}
