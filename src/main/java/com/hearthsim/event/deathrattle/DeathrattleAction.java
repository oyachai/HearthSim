package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.entity.BaseEntity;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

/**
 * Actions triggered by a deathrattle
 *
 */
public abstract class DeathrattleAction {
	
	/**
	 * Perform the action
	 *  @param minion The minion that is performing the action (aka, the dying minion)
	 * @param playerSide
     * @param boardState
     * @param deckPlayer0
     * @param deckPlayer1    @return
     * @throws HSInvalidPlayerIndexException
	 */
	public HearthTreeNode performAction(BaseEntity minion, PlayerSide playerSide, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		return boardState;
	}
	
}
