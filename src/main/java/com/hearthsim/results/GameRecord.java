package com.hearthsim.results;

import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthActionBoardPair;
import org.json.JSONObject;

import java.util.List;

public interface GameRecord {

    /**
     * Put a record
     * @param turn The turn number
     * @param activePlayerSide Index of the player that just played a turn
     * @param board
     */
    public void put(int turn, PlayerSide activePlayerSide, BoardModel board, List<HearthActionBoardPair> plays);

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
     * @param playerId The id of the player for which to return the data
     * @param turn Turn number
     * @param currentPlayerId The id of the player that just played a turn
     * @return
     */
    public int getNumMinions(int playerId, int turn, int currentPlayerId);

    /**
     * Returns the number of cards in the hand of a given player
     *
     * @param playerId The id of the player for which to return the data
     * @param turn Turn number
     * @param currentPlayerId The id of the player that just played a turn
     * @return
     */
    public int getNumCardsInHand(int playerId, int turn, int currentPlayerId);

    /**
     * Get the health of a given player's hero
     *
     * @param playerId The id of the player for which to return the data
     * @param turn Turn number
     * @param currentPlayerId The id of the player that just played a turn
     * @return
     */
    public int getHeroHealth(int playerId, int turn, int currentPlayerId);

    /**
     * Get the armor of a given player's hero
     *
     * @param playerId The id of the player for which to return the data
     * @param turn Turn number
     * @param currentPlayerId The id of the player that just played a turn
     * @return
     */
    public int getHeroArmor(int playerId, int turn, int currentPlayerId);





    /**
     * Returns the JSON representation of this record
     * @return
     */
    public JSONObject toJSON();

}
