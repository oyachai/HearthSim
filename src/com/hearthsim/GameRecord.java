package com.hearthsim;

import java.util.ArrayList;

import com.hearthsim.util.BoardState;
import com.json.JSONArray;
import com.json.JSONObject;

public class GameRecord {
	
	ArrayList<ArrayList<BoardState>> boards_;
	
	public GameRecord() {
		boards_ = new ArrayList<ArrayList<BoardState>>(2);
		boards_.add(new ArrayList<BoardState>());
		boards_.add(new ArrayList<BoardState>());
	}
	
	public void put(int playerID, BoardState board) {
		boards_.get(playerID).add(board);
	}
	
	public BoardState get(int turn, int playerID) {
		return boards_.get(playerID).get(turn);
	}
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		
		JSONArray b0 = new JSONArray();
		for (BoardState board : boards_.get(0)) {
			b0.put(board.toJSON());
		}

		JSONArray b1 = new JSONArray();
		for (BoardState board : boards_.get(1)) {
			b1.put(board.toJSON());
		}
		
		json.put("p0", b0);
		json.put("p1", b1);
		return json;
	}
}
