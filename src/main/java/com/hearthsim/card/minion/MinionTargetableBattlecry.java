package com.hearthsim.card.minion;

import java.util.EnumSet;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion.BattlecryTargetType;
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
	 * @param deckPlayer0
	 * @param deckPlayer1
	 * @return
	 * @throws HSException
	 */
	public HearthTreeNode useTargetableBattlecry_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState,
			Deck deckPlayer0, Deck deckPlayer1) throws HSException;

	public EnumSet<BattlecryTargetType> getBattlecryTargets();
}
