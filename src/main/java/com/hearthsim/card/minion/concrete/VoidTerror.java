package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.IdentityLinkedList;
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
        IdentityLinkedList<Minion> minions = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getMinions();
        int thisMinionIndex = minions.indexOf(this);
        if (thisMinionIndex > 0) {
            Minion minionToDestroy = minions.get(thisMinionIndex - 1);
            this.addAttack(minionToDestroy.getTotalAttack());
            this.addHealth(minionToDestroy.getTotalHealth());
            this.addMaxHealth(minionToDestroy.getTotalHealth());
            minionToDestroy.setHealth((byte)-99);
        }
        if (thisMinionIndex < minions.size() - 1) {
            Minion minionToDestroy = minions.get(thisMinionIndex + 1);
            this.addAttack(minionToDestroy.getTotalAttack());
            this.addHealth(minionToDestroy.getTotalHealth());
            this.addMaxHealth(minionToDestroy.getTotalHealth());
            minionToDestroy.setHealth((byte)-99);
        }
        return boardState;
    }

}
