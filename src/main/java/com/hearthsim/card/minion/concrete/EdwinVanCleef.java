package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.util.tree.HearthTreeNode;

/**
 * Created by oyachai on 3/21/15.
 */
public class EdwinVanCleef extends Minion implements MinionUntargetableBattlecry {

    public EdwinVanCleef() {
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

        byte buff = (byte) ((boardState.data_.getCurrentPlayer().getNumCardsUsed() - 1) * 2);
        this.addAttack(buff);
        this.addHealth(buff);
        this.addMaxHealth(buff);
        return boardState;
    }

}
