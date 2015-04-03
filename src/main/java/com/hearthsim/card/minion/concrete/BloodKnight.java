package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.util.tree.HearthTreeNode;

public class BloodKnight extends Minion implements MinionUntargetableBattlecry {

    public BloodKnight() {
        super();
    }

    /**
     * Battlecry: All minions lose Divine Shield.  Gain +3/+3 for each Shield lost
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            int minionPlacementIndex,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) {
        for (Minion minion : boardState.data_.getAllMinions()) {
            if (minion != this && minion.getDivineShield()) {
                minion.setDivineShield(false);
                this.setHealth((byte)(this.getHealth() + 3));
                this.setAttack((byte)(this.getAttack() + 3));
            }
        }
        return boardState;
    }
}
