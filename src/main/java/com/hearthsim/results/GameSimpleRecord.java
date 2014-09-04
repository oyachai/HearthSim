package com.hearthsim.results;

import com.hearthsim.exception.HSException;
import com.hearthsim.util.boardstate.BoardState;
import org.json.JSONObject;

public class GameSimpleRecord implements GameRecord {

	int maxTurns_;
	byte[][][] numMinions_;
	byte[][][] numCards_;
	byte[][][] heroHealth_;
	byte[][][] heroArmor_;
	
	public GameSimpleRecord() {
		this(50);
	}
	
	public GameSimpleRecord(int maxTurns) {
		maxTurns_ = maxTurns;
		numMinions_ = new byte[2][maxTurns][2];
		numCards_ = new byte[2][maxTurns][2];
		heroHealth_ = new byte[2][maxTurns][2];
		heroArmor_ = new byte[2][maxTurns][2];
	}
	
	@Override
	public void put(int turn, int activePlayerIndex, BoardState board) {
		try {
			int inactivePlayerIndex = (activePlayerIndex + 1) % 2;
			
			numMinions_[activePlayerIndex][turn][activePlayerIndex] = (byte)board.getNumMinions(0);
			numMinions_[activePlayerIndex][turn][inactivePlayerIndex] = (byte)board.getNumMinions(1);

			numCards_[activePlayerIndex][turn][activePlayerIndex] = (byte)board.getNumCards_hand(0);
			numCards_[activePlayerIndex][turn][inactivePlayerIndex] = (byte)board.getNumCards_hand(1);

			heroHealth_[activePlayerIndex][turn][activePlayerIndex] = board.getHero(0).getHealth();
			heroHealth_[activePlayerIndex][turn][inactivePlayerIndex] = board.getHero(1).getHealth();

			heroArmor_[activePlayerIndex][turn][activePlayerIndex] = board.getHero(0).getArmor();
			heroArmor_[activePlayerIndex][turn][inactivePlayerIndex] = board.getHero(1).getArmor();

		} catch (HSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getRecordLength(int playerIndex) {
		return maxTurns_;
	}

	@Override
	public int getNumMinions(int playerIndex, int turn, int activePlayerIndex) {
		return numMinions_[activePlayerIndex][turn][playerIndex];
	}

	@Override
	public int getNumCardsInHand(int playerIndex, int turn, int activePlayerIndex) {
		return numCards_[activePlayerIndex][turn][playerIndex];
	}

	@Override
	public int getHeroHealth(int playerIndex, int turn, int activePlayerIndex) {
		return heroHealth_[activePlayerIndex][turn][playerIndex];
	}

	@Override
	public int getHeroArmor(int playerIndex, int turn, int activePlayerIndex) {
		return heroArmor_[activePlayerIndex][turn][playerIndex];
	}

	@Override
	public JSONObject toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

}
