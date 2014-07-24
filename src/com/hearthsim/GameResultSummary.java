package com.hearthsim;

import com.hearthsim.util.BoardState;
import com.json.JSONArray;
import com.json.JSONObject;

public class GameResultSummary {

	GameResult result_;
	
	public GameResultSummary(GameResult result) {
		result_ = result;
	}
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		
		json.put("winner", result_.winnerPlayerIndex_);
		json.put("duration", result_.gameDuration_);
		
		JSONObject record = new JSONObject();
		JSONArray p0 = new JSONArray();
		for (int i = 0; i < result_.record_.getRecordLength(0); ++i) {
			BoardState board = result_.record_.get(i, 0);
			JSONObject tj = new JSONObject();
			tj.put("p0_m", board.getNumMinions_p0());
			tj.put("p1_m", board.getNumMinions_p1());
			tj.put("p0_c", board.getNumCards_hand_p0());
			tj.put("p1_c", board.getNumCards_hand_p1());
			tj.put("p0_h", board.getHero_p0().getHealth());
			tj.put("p1_h", board.getHero_p1().getHealth());
			p0.put(tj);
		}
		
		JSONArray p1 = new JSONArray();
		for (int i = 0; i < result_.record_.getRecordLength(1); ++i) {
			BoardState board = result_.record_.get(i, 1);
			JSONObject tj = new JSONObject();
			tj.put("p0_m", board.getNumMinions_p0());
			tj.put("p1_m", board.getNumMinions_p1());
			tj.put("p0_c", board.getNumCards_hand_p0());
			tj.put("p1_c", board.getNumCards_hand_p1());
			tj.put("p0_h", board.getHero_p0().getHealth());
			tj.put("p1_h", board.getHero_p1().getHealth());
			p1.put(tj);
		}

		record.put("p0", p0);
		record.put("p1", p1);
		
		json.put("record", record);
		return json;
	}
}
