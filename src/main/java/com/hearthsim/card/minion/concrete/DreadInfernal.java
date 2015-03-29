package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class DreadInfernal extends Minion implements MinionUntargetableBattlecry {

    public DreadInfernal() {
        super();
    }

    /**
     * Battlecry: Deals 1 damage to all characters
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            int minionPlacementIndex,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) {
        HearthTreeNode toRet = boardState;
        PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = toRet.data_.modelForSide(PlayerSide.WAITING_PLAYER);
        toRet = currentPlayer.getHero().takeDamageAndNotify((byte) 1, PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, toRet, false, false);
        for (Minion minion : currentPlayer.getMinions()) {
            if (minion != this)
                toRet = minion.takeDamageAndNotify((byte) 1, PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, boardState, false, false);
        }

        toRet = waitingPlayer.getHero().takeDamageAndNotify((byte)1, PlayerSide.CURRENT_PLAYER, PlayerSide.WAITING_PLAYER, boardState, false, false);
        for (Minion minion : waitingPlayer.getMinions()) {
            toRet = minion.takeDamageAndNotify((byte) 1, PlayerSide.CURRENT_PLAYER, PlayerSide.WAITING_PLAYER, boardState, false, false);
        }

        return toRet;
    }

}
