package com.hearthsim;

public class GameResult {

	public final int winnerPlayerIndex_;
	public final int gameDuration_;
	
	public GameResult(int winnerPlayerIndex, int gameDuration) {
		winnerPlayerIndex_ = winnerPlayerIndex;
		gameDuration_ = gameDuration;
	}
}
