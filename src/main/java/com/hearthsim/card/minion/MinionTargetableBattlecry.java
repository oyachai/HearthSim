package com.hearthsim.card.minion;

import java.util.EnumSet;

import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public interface MinionTargetableBattlecry {
    /**
     * Derived classes should implement this function for targtable battlecries.
     *
     * @param side
     * @param targetMinion
     * @param boardState
     * @return
     * @throws HSException
     */
    public HearthTreeNode useTargetableBattlecry_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState) throws HSException;

    public EnumSet<BattlecryTargetType> getBattlecryTargets();
}
