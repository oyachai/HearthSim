package com.hearthsim.results;

import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.TreeMap;

public class GameDetailedRecord implements GameRecord {
	
	ArrayList<TreeMap<Integer, BoardModel>> boards_;
	
	public GameDetailedRecord() {
		boards_ = new ArrayList<TreeMap<Integer, BoardModel>>(2);
		boards_.add(new TreeMap<Integer, BoardModel>());
		boards_.add(new TreeMap<Integer, BoardModel>());
	}
	
	@Override
	public void put(int turn, PlayerModel activePlayerModel, BoardModel board) {
        int index = board.getIndexOfPlayer(activePlayerModel);
        boards_.get(index).put(turn, board);
	}
	
	@Override
	public int getRecordLength(int playerID) {
		return boards_.get(playerID).size();
	}

	@Override
    public int getNumMinions(int playerIndex, int turn, int activePlayerIndex) {
        BoardModel boardModel = boards_.get(activePlayerIndex).get(turn);
        PlayerModel playerByIndex = boardModel.getPlayerByIndex(playerIndex);
        PlayerModel otherPlayer = boardModel.getOtherPlayer(playerByIndex);
        return otherPlayer.getNumMinions();
    }

	@Override
	public int getNumCardsInHand(int playerIndex, int turn, int activePlayerIndex) {
		try {
            BoardModel boardModel = boards_.get(activePlayerIndex).get(turn);
            PlayerModel playerByIndex = boardModel.getPlayerByIndex(playerIndex);
            PlayerModel otherPlayer = boardModel.getOtherPlayer(playerByIndex);
            return boardModel.getNumCards_hand(otherPlayer);
		} catch (HSInvalidPlayerIndexException e) {
			return 0;
		}
	}

	@Override
	public int getHeroHealth(int playerIndex, int turn, int activePlayerIndex) {
		try {

            BoardModel boardModel = boards_.get(activePlayerIndex).get(turn);
            PlayerModel playerByIndex = boardModel.getPlayerByIndex(playerIndex);
            PlayerModel otherPlayer = boardModel.getOtherPlayer(playerByIndex);
            return boardModel.getHero(otherPlayer).getHealth();
		} catch (HSInvalidPlayerIndexException e) {
			return 0;
		}
	}

	@Override
	public int getHeroArmor(int playerIndex, int turn, int activePlayerIndex) {
		try {
            BoardModel boardModel = boards_.get(activePlayerIndex).get(turn);
            PlayerModel playerByIndex = boardModel.getPlayerByIndex(playerIndex);
            PlayerModel otherPlayer = boardModel.getOtherPlayer(playerByIndex);
            return boardModel.getHero(otherPlayer).getArmor();
		} catch (HSInvalidPlayerIndexException e) {
			return 0;
		}
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		return json;
	}
	
	public BoardModel get(int turn, int playerID) {
		return boards_.get(playerID).get(turn);
	}

}
