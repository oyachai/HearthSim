package com.hearthsim.player.playercontroller;

import java.util.List;

import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.HearthActionBoardPair;
import com.hearthsim.util.factory.BoardStateFactoryBase;

public interface ArtificialPlayer extends DeepCopyable<ArtificialPlayer> {

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
	 * @return A list of HearthActionBoardPair that the AI has performed, starting from the earliest play to the last.
	 * @throws HSException
	 */
	public List<HearthActionBoardPair> playTurn(int turn, BoardModel board) throws HSException;
	
	
	/**
	 * Play a turn
	 * 
	 * This function is called by GameMaster, and it should return a BoardState resulting from the AI playing its turn.
	 * 
	 * @param turn Turn number, 1-based
	 * @param board The board state at the beginning of the turn (after all card draws and minion deaths)
	 * @param factory The factory to use for node generation
	 * 
	 * @return A list of HearthActionBoardPair that the AI has performed, starting from the earliest play to the last.
	 * @throws HSException
	 */
	public List<HearthActionBoardPair> playTurn(int turn, BoardModel board, BoardStateFactoryBase factory) throws HSException;
	
	public int getMaxThinkTime();
}
