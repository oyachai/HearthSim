package com.hearthsim.card.minion;

import com.hearthsim.util.tree.HearthTreeNode;

public interface MinionUntargetableBattlecry {
    public HearthTreeNode useUntargetableBattlecry_core(int minionPlacementIndex, HearthTreeNode boardState, boolean singleRealizationOnly);
}
