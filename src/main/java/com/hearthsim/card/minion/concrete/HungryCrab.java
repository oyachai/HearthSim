package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.BattlecryTargetType;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.EnumSet;

public class HungryCrab extends Minion implements MinionTargetableBattlecry {

    public HungryCrab() {
        super();
    }

    @Override
    public EnumSet<BattlecryTargetType> getBattlecryTargets() {
        return EnumSet.of(BattlecryTargetType.FRIENDLY_MINIONS, BattlecryTargetType.ENEMY_MINIONS);
    }

    /**
     * Battlecry: Destroy a murloc and gain +2/+2
     */
    @Override
    public HearthTreeNode useTargetableBattlecry_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState) throws HSException {
        if (targetMinion.getTribe() == MinionTribe.MURLOC) {
            targetMinion.setHealth((byte)-99);
            this.addAttack((byte) 2);
            this.addHealth((byte) 2);
            this.addMaxHealth((byte) 2);
            return boardState;
        } else {
            return null;
        }
    }
}
