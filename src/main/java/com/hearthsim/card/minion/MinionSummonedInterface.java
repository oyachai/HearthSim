package com.hearthsim.card.minion;

import com.hearthsim.card.Deck;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public interface MinionSummonedInterface {
	/**
	 * Called whenever another minion is summoned
	 *
	 * @param thisMinionPlayerSide
	 * @param summonedMinionPlayerSide
	 * @param summonedMinion The summoned minion
	 * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer1 The deck of player1
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionSummonEvent(PlayerSide thisMinionPlayerSide, PlayerSide summonedMinionPlayerSide,
			Minion summonedMinion, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1);
}
