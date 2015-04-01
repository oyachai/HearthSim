package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class LeeroyJenkins extends Minion implements MinionUntargetableBattlecry {

    public LeeroyJenkins() {
        super();
    }

    /**
     * Battlecry: summon two 1/1 whelps for your opponent
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            int minionPlacementIndex,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) {
        HearthTreeNode toRet = boardState;
        PlayerModel waitingPlayer = toRet.data_.modelForSide(PlayerSide.WAITING_PLAYER);
        for (int index = 0; index < 2; ++index) {
            if (!waitingPlayer.isBoardFull()) {
                Minion newMinion = new Whelp();
                toRet = newMinion.summonMinionAtEnd(PlayerSide.WAITING_PLAYER, toRet, false, true);
            }
        }
        return toRet;
    }
}
