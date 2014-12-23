package com.hearthsim.card.minion;

import com.hearthsim.card.Deck;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public interface MinionHealedInterface {
	/**
	 * Called whenever another character (including the hero) is healed
	 * 
	 * @param thisMinionPlayerSide
	 * @param healedMinionPlayerSide
	 * @param healedMinion The healed minion
	 * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0 @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionHealedEvent(PlayerSide thisMinionPlayerSide, PlayerSide healedMinionPlayerSide,
			Minion healedMinion, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1_);
}
