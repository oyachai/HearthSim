package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.BattlecryTargetType;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.EnumSet;

public class CruelTaskmaster extends Minion implements MinionTargetableBattlecry {

    public CruelTaskmaster() {
        super();
    }

    @Override
    public EnumSet<BattlecryTargetType> getBattlecryTargets() {
        return EnumSet.of(BattlecryTargetType.FRIENDLY_MINIONS, BattlecryTargetType.ENEMY_MINIONS);
    }

    /**
     * Battlecry: Deal 1 damage to a minion and give it +2 Attack
     */
    @Override
    public HearthTreeNode useTargetableBattlecry_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState) throws HSException {
        HearthTreeNode toRet = boardState;
        targetMinion.setAttack((byte)(targetMinion.getAttack() + 2));
        toRet = targetMinion.takeDamageAndNotify((byte) 1, PlayerSide.CURRENT_PLAYER, side, toRet, false, true);
        return toRet;
    }
}
