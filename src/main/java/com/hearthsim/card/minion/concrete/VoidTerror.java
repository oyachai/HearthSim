package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class VoidTerror extends Minion implements MinionUntargetableBattlecry {

    public VoidTerror() {
        super();
    }

    /**
     * Battlecry: Destroy the minions on either side of this minion and gain their Attack and Health.
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            int minionPlacementIndex,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) {
        PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);

        int thisMinionIndex = currentPlayer.getIndexForCharacter(this);
        for (Minion minion : currentPlayer.getMinionsAdjacentToCharacter(thisMinionIndex)) {
            this.addAttack(minion.getTotalAttack());
            this.addHealth(minion.getTotalHealth());
            this.addMaxHealth(minion.getTotalHealth());
            minion.setHealth((byte) -99);
        }
        return boardState;
    }
}
