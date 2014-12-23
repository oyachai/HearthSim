package com.hearthsim.card.minion;

import com.hearthsim.card.Deck;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.tree.HearthTreeNode;

public interface MinionUntargetableBattlecry {
	public HearthTreeNode useUntargetableBattlecry_core(Minion minionPlacementTarget, HearthTreeNode boardState,
			Deck deckPlayer0, Deck deckPlayer1, boolean singleRealizationOnly) throws HSException;
}
