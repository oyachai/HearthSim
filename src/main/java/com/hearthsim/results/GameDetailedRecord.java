package com.hearthsim.results;

import java.util.ArrayList;
import java.util.TreeMap;

import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;

import org.json.JSONObject;

public class GameDetailedRecord implements GameRecord {
	
	ArrayList<TreeMap<Integer, BoardState>> boards_;
	
	public GameDetailedRecord() {
		boards_ = new ArrayList<TreeMap<Integer, BoardState>>(2);
		boards_.add(new TreeMap<Integer, BoardState>());
		boards_.add(new TreeMap<Integer, BoardState>());
	}
	
	@Override
	public void put(int turn, int activePlayerIndex, BoardState board) {
		boards_.get(activePlayerIndex).put(turn, board);
	}
	
	@Override
	public int getRecordLength(int playerID) {
		return boards_.get(playerID).size();
	}

	@Override
	public int getNumMinions(int playerIndex, int turn, int activePlayerIndex) {
		try {
			return boards_.get(activePlayerIndex).get(turn).getNumMinions((playerIndex + activePlayerIndex) % 2);
		} catch (HSInvalidPlayerIndexException e) {
			return 0;
		}
	}

	@Override
	public int getNumCardsInHand(int playerIndex, int turn, int activePlayerIndex) {
		try {
			return boards_.get(activePlayerIndex).get(turn).getNumCards_hand((playerIndex + activePlayerIndex) % 2);
		} catch (HSInvalidPlayerIndexException e) {
			return 0;
		}
	}

	@Override
	public int getHeroHealth(int playerIndex, int turn, int activePlayerIndex) {
		try {
			return boards_.get(activePlayerIndex).get(turn).getHero((playerIndex + activePlayerIndex) % 2).getHealth();
		} catch (HSInvalidPlayerIndexException e) {
			return 0;
		}
	}

	@Override
	public int getHeroArmor(int playerIndex, int turn, int activePlayerIndex) {
		try {
			return boards_.get(activePlayerIndex).get(turn).getHero((playerIndex + activePlayerIndex) % 2).getArmor();
		} catch (HSInvalidPlayerIndexException e) {
			return 0;
		}
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		return json;
	}
	
	public BoardState get(int turn, int playerID) {
		return boards_.get(playerID).get(turn);
	}

}
