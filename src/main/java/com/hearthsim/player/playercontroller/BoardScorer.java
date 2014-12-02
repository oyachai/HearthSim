package com.hearthsim.player.playercontroller;

import com.hearthsim.card.Card;
import com.hearthsim.model.BoardModel;

public interface BoardScorer {
	/**
	 * Board score function
	 * The all important board score function. It is a function that measures how 'good' the given board is.
	 * As a convention, this function should be an increasing function of the board's goodness.
	 * 
	 * @param board The current board state
	 * @return
	 */
	public double boardScore(BoardModel board);
	
	/**
	 * Returns the card score for a particular card assuming that it is in the hand
	 * 
	 * @param card
	 * @return
	 */
	public double cardInHandScore(Card card);

	public double heroHealthScore_p0(double heroHealth, double heroArmor);
	
	public double heroHealthScore_p1(double heroHealth, double heroArmor);
}
