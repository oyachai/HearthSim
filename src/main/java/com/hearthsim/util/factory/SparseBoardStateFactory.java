package com.hearthsim.util.factory;

import com.hearthsim.card.Deck;

/**
 * A BoardStateFactory in which the minion placement is simplified in order to reduce simulation time
 */
public class SparseBoardStateFactory extends DepthBoardStateFactory {

	/**
	 * Constructor
	 * maxThinkTime defaults to 10000 milliseconds (10 seconds)
	 */
	public SparseBoardStateFactory(Deck deckPlayer0, Deck deckPlayer1) {
		this(deckPlayer0, deckPlayer1, 10000);
	}

	/**
	 * Constructor
	 * 
	 * @param deckPlayer0
	 * @param deckPlayer1
	 * @param maxThinkTime The maximum amount of time in milliseconds the factory is allowed to spend on generating the simulation tree.
	 */
	public SparseBoardStateFactory(Deck deckPlayer0, Deck deckPlayer1, long maxThinkTime) {
		super(deckPlayer0, deckPlayer1, maxThinkTime, new SparseChildNodeCreator(deckPlayer0, deckPlayer1));
	}
}
