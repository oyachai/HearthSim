package com.hearthsim.event;

import com.hearthsim.GameResult;

public class HSGameEndEvent {

	private GameResult result_;
	
	HSGameEndEvent(GameResult result) {
		result_ = result;
	}
	
	GameResult getResult() {
		return result_;
	}
}
