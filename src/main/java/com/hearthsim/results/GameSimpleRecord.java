package com.hearthsim.results;

import com.hearthsim.card.minion.Hero;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
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
	public void put(int turn, PlayerSide activePlayerSide, BoardModel board) {
        int activePlayerIndex;
        int inactivePlayerIndex;
        //todo: I don't think this is correct.. perhaps we need to store a unique player ID in the playermodel?
        if (PlayerSide.CURRENT_PLAYER== activePlayerSide) {
            activePlayerIndex = 0;
            inactivePlayerIndex = 1;
        } else {
            activePlayerIndex = 1;
            inactivePlayerIndex = 0;
        }

        numMinions_[activePlayerIndex][turn][activePlayerIndex] = (byte)board.getCurrentPlayer().getNumMinions();
        numMinions_[activePlayerIndex][turn][inactivePlayerIndex] = (byte)board.getWaitingPlayer().getNumMinions();

        numCards_[activePlayerIndex][turn][activePlayerIndex] = (byte)board.getNumCardsHandCurrentPlayer();
        numCards_[activePlayerIndex][turn][inactivePlayerIndex] = (byte)board.getNumCardsHandWaitingPlayer();

        Hero currentPlayerHero = board.getCurrentPlayerHero();
        heroHealth_[activePlayerIndex][turn][activePlayerIndex] = currentPlayerHero.getHealth();
        Hero waitingPlayerHero = board.getWaitingPlayerHero();
        heroHealth_[activePlayerIndex][turn][inactivePlayerIndex] = waitingPlayerHero.getHealth();

        heroArmor_[activePlayerIndex][turn][activePlayerIndex] = currentPlayerHero.getArmor();
        heroArmor_[activePlayerIndex][turn][inactivePlayerIndex] = waitingPlayerHero.getArmor();
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
