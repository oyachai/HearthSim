package com.hearthsim.results;

import com.hearthsim.util.boardstate.BoardState;
import org.json.JSONObject;

public interface GameRecord {
	
	/**
	 * Put a record
	 * 
	 * @param turn The turn number
	 * @param activePlayerIndex Index of the player that just played a turn
	 * @param board
	 */
	public void put(int turn, int activePlayerIndex, BoardState board);
	
	/**
	 * Return the number of turns recorded for each player
	 * 
	 * @param playerIndex
	 * @return
	 */
	public int getRecordLength(int playerIndex);
	
	
	/**
	 * Returns the number of minions on a player's board
	 * 
	 * @param playerIndex The index of the player for which to return the data
	 * @param turn Turn number
	 * @param activePlayerIndex The index of the player that just played a turn
	 * @return
	 */
	public int getNumMinions(int playerIndex, int turn, int activePlayerIndex);
	
	/**
	 * Returns the number of cards in the hand of a given player
	 * 
	 * @param playerIndex The index of the player for which to return the data
	 * @param turn Turn number
	 * @param activePlayerIndex The index of the player that just played a turn
	 * @return
	 */
	public int getNumCardsInHand(int playerIndex, int turn, int activePlayerIndex);
	
	/**
	 * Get the health of a given player's hero
	 * 
	 * @param playerIndex The index of the player for which to return the data
	 * @param turn Turn number
	 * @param activePlayerIndex The index of the player that just played a turn
	 * @return
	 */
	public int getHeroHealth(int playerIndex, int turn, int activePlayerIndex);
	
	/**
	 * Get the armor of a given player's hero
	 * 
	 * @param playerIndex The index of the player for which to return the data
	 * @param turn Turn number
	 * @param activePlayerIndex The index of the player that just played a turn
	 * @return
	 */
	public int getHeroArmor(int playerIndex, int turn, int activePlayerIndex);
	
	
	
	
	
	/**
	 * Returns the JSON representation of this record
	 * @return
	 */
	public JSONObject toJSON();

}
