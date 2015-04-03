package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.util.tree.HearthTreeNode;

public class TwilightDrake extends Minion implements MinionUntargetableBattlecry {

    public TwilightDrake() {
        super();
    }

    /**
     * Battlecry: Gain +1 Health for each card in your hand.
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            int minionPlacementIndex,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) {
        this.addHealth((byte) boardState.data_.getCurrentPlayer().getHand().size());
        return boardState;
    }

}
