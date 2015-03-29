package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.util.tree.HearthTreeNode;

public class Felguard extends Minion implements MinionUntargetableBattlecry {

    public Felguard() {
        super();
    }

    /**
     * Taunt.  Battlecry: Destroy one of your Mana Crystals
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            int minionPlacementIndex,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) {
        HearthTreeNode toRet = boardState;
        toRet.data_.getCurrentPlayer().subtractMaxMana((byte)1);
        return toRet;
    }

}
