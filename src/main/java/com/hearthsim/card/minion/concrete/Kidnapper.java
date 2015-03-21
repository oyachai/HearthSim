package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.BattlecryTargetType;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.EnumSet;

/**
 * Created by oyachai on 3/21/15.
 */
public class Kidnapper extends Minion implements MinionTargetableBattlecry {

    public Kidnapper() {
        super();
    }

    @Override
    public boolean hasBattlecry() {
        return false;
    }

    @Override
    public EnumSet<BattlecryTargetType> getBattlecryTargets() {
        return EnumSet.of(BattlecryTargetType.FRIENDLY_MINIONS, BattlecryTargetType.ENEMY_MINIONS);
    }

    @Override
    public HearthTreeNode useTargetableBattlecry_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState) throws HSException {
        HearthTreeNode toRet = boardState;
        if (toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER).isComboEnabled()) {
            if (!toRet.data_.modelForSide(side).isHandFull()) {
                Minion copy = targetMinion.createResetCopy();
                toRet.data_.modelForSide(side).placeCardHand(copy);
                toRet.data_.removeMinion(targetMinion);
            } else {
                targetMinion.setHealth((byte)-99);
            }
        }
        return toRet;
    }


}
