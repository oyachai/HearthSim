package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class PitLord extends Minion implements MinionUntargetableBattlecry {


    public PitLord() {
        super();
    }

    /**
     * Battlecry: Deal 5 damage to your hero
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            int minionPlacementIndex,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) {
        HearthTreeNode toRet = boardState;
        PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        toRet = currentPlayer.getHero().takeDamageAndNotify((byte)5, PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, boardState, false, true);
        return toRet;
    }
}
