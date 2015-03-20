package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Nightblade extends Minion implements MinionUntargetableBattlecry {

    public Nightblade() {
        super();
    }

    /**
     * Battlecry: Deal 4 damage to himself
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            int minionPlacementIndex,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) throws HSException {
        HearthTreeNode toRet = boardState;
        toRet = toRet.data_.getWaitingPlayer().getHero().takeDamage((byte)3, PlayerSide.CURRENT_PLAYER, PlayerSide.WAITING_PLAYER, boardState, false, false);
        return toRet;
    }

}
