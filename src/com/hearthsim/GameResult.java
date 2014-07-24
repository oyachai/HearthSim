package com.hearthsim;

public class GameResult {

	public final int winnerPlayerIndex_;
	public final int gameDuration_;
	public final GameRecord record_;
	
	public GameResult(int winnerPlayerIndex, int gameDuration, GameRecord record) {
		winnerPlayerIndex_ = winnerPlayerIndex;
		gameDuration_ = gameDuration;
		record_ = record;
	}
}
