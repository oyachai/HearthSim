package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class FrostwolfWarlord extends Minion implements MinionUntargetableBattlecry {

    public FrostwolfWarlord() {
        super();
    }

    /**
     * Battlecry: gain +1/+1 for each friendly minion on the battlefield
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            int minionPlacementIndex,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) {
        int numBuffs = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getNumMinions() - 1; //Don't count the Warlord itself
        this.setAttack((byte)(this.getAttack() + numBuffs));
        this.setHealth((byte)(this.getHealth() + numBuffs));
        this.setMaxHealth((byte)(this.getMaxHealth() + numBuffs));
        return boardState;
    }

}
