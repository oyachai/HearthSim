package com.hearthsim.card.minion;

import com.hearthsim.exception.HSException;
import com.hearthsim.util.tree.HearthTreeNode;

public interface MinionUntargetableBattlecry {
    public HearthTreeNode useUntargetableBattlecry_core(Minion minionPlacementTarget, HearthTreeNode boardState, boolean singleRealizationOnly) throws HSException;
}
