package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

/**
 * Created by oyachai on 3/21/15.
 */
public class DefiasRingleader extends Minion implements MinionUntargetableBattlecry {

    public DefiasRingleader() {
        super();
    }

    @Override
    public boolean hasBattlecry() {
        return false;
    }

    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
        int minionPlacementIndex,
        HearthTreeNode boardState,
        boolean singleRealizationOnly
    ) {
        HearthTreeNode toRet = boardState;

        if (toRet.data_.getCurrentPlayer().isComboEnabled()) {
            Minion newMinion = new DefiasBandit();
            toRet = newMinion.summonMinion(PlayerSide.CURRENT_PLAYER, this, toRet, false, true);
        }
        return toRet;
    }

}
