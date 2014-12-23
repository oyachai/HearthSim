package com.hearthsim.card.minion;

import com.hearthsim.card.Deck;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public interface MinionDeadInterface {
	/**
	 * 
	 * Called whenever another minion dies
	 * 
	 *
	 * @param thisMinionPlayerSide
	 * @param deadMinionPlayerSide
	 * @param deadMinion The dead minion
	 * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0 @return The boardState is manipulated and returned
	 * */
	public HearthTreeNode minionDeadEvent(PlayerSide thisMinionPlayerSide, PlayerSide deadMinionPlayerSide,
			Minion deadMinion, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1);
}
