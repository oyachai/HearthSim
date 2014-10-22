package com.hearthsim.player.playercontroller;

import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.util.DeepCopyable;

public interface ArtificialPlayer extends DeepCopyable {

	/**
	 * Board score function
	 * 
	 * The all important board score function.  It is a function that measures how 'good' the given board is. 
	 * As a convention, this function should be an increasing function of the board's goodness.
	 * 
	 * @param board The current board state
	 * @return
	 */
	public double boardScore(BoardModel board);

	
	/**
	 * Play a turn
	 * 
	 * This function is called by GameMaster, and it should return a BoardState resulting from the AI playing its turn.
	 * 
	 * @param turn Turn number, 1-based
	 * @param board The board state at the beginning of the turn (after all card draws and minion deaths)
	 * 
	 * @return
	 * @throws HSException
	 */
	public BoardModel playTurn(int turn, BoardModel board) throws HSException;
	
	
	/**
	 * Play a turn
	 * 
	 * This function is called by GameMaster, and it should return a BoardState resulting from the AI playing its turn.
	 * 
	 * @param turn Turn number, 1-based
	 * @param board The board state at the beginning of the turn (after all card draws and minion deaths)
	 * @param maxThinkTime The maximum number of milliseconds the AI will spend per tree
	 * 
	 * @return
	 * @throws HSException
	 */
	public BoardModel playTurn(int turn, BoardModel board, int maxThinkTime) throws HSException;
		
	
	public int getMaxThinkTime();
}
