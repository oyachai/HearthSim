package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class DarkscaleHealer extends Minion implements MinionUntargetableBattlecry {

    public DarkscaleHealer() {
        super();
    }

    /**
     * Battlecry: Heals friendly characters for 2
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            int minionPlacementIndex,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) throws HSException {
        HearthTreeNode toRet = boardState;
        PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        toRet = currentPlayer.getHero().takeHeal((byte)2, PlayerSide.CURRENT_PLAYER, toRet);
        for (Minion minion : currentPlayer.getMinions()) {
            toRet = minion.takeHeal((byte)2, PlayerSide.CURRENT_PLAYER, toRet);
        }
        return toRet;
    }

}
