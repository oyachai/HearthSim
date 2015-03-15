package com.hearthsim.card.minion.concrete;

import java.util.EnumSet;

import com.hearthsim.card.minion.BattlecryTargetType;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class FrostElemental extends Minion implements MinionTargetableBattlecry {

    public FrostElemental() {
        super();
    }

    /**
     * Let's assume that it is never a good idea to freeze your own character
     */
    @Override
    public EnumSet<BattlecryTargetType> getBattlecryTargets() {
        return EnumSet.of(BattlecryTargetType.ENEMY_HERO, BattlecryTargetType.ENEMY_MINIONS);
    }

    /**
     * Battlecry: Freeze a character
     */
    @Override
    public HearthTreeNode useTargetableBattlecry_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState) throws HSException {
        targetMinion.setFrozen(true);
        return boardState;
    }

}
