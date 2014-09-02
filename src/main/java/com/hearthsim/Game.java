package com.hearthsim;

import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.player.Player;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.player.playercontroller.GameMaster;
import com.hearthsim.results.GameRecord;
import com.hearthsim.results.GameResult;
import com.hearthsim.results.GameSimpleRecord;
import com.hearthsim.util.boardstate.BoardState;

public class Game {

	final static int maxTurns_ = 100;
	
	BoardState boardState_;
	Player[] players_;
	GameMaster[] gms_;

	int curPlayer_;
	int curTurn_;
	
	int s0_; //player index of the player that goes first
	int s1_; //player index of the player that goes second
	
	public Game(Player player0, Player player1, ArtificialPlayer ai0, ArtificialPlayer ai1) {
		this(player0, player1, ai0, ai1, false);
	}
	
	public Game(Player player0, Player player1, ArtificialPlayer ai0, ArtificialPlayer ai1, boolean shufflePlayOrder) {
		s0_ = 0;
		s1_ = 1;
        if (shufflePlayOrder && Math.random() > 0.5) {
            s0_ = 1;
            s1_ = 0;
        }
        players_ = new Player[2];
		players_[0] = player0;
		players_[1] = player1;
		boardState_ = new BoardState(players_[s0_].hero_, players_[s1_].hero_);
		gms_ = new GameMaster[2];
		gms_[0] = new GameMaster(ai0);
		gms_[1] = new GameMaster(ai1);
	}
		
	public BoardState getBoardState(int playerID) {
		if (playerID == curPlayer_)
			return boardState_;
		else
			return boardState_.flipPlayers();
	}
	
	public GameResult runGame() throws HSException {
		curTurn_ = 0;
		curPlayer_ = 0;
		
		//the first player draws 3 cards
		boardState_.placeCard_hand_p0(players_[s0_].drawFromDeck(0));
		boardState_.placeCard_hand_p0(players_[s0_].drawFromDeck(1));
		boardState_.placeCard_hand_p0(players_[s0_].drawFromDeck(2));
		boardState_.setDeckPos_p0(3);

		//the second player draws 4 cards
		boardState_.placeCard_hand_p1(players_[s1_].drawFromDeck(0));
		boardState_.placeCard_hand_p1(players_[s1_].drawFromDeck(1));
		boardState_.placeCard_hand_p1(players_[s1_].drawFromDeck(2));
		boardState_.placeCard_hand_p1(players_[s1_].drawFromDeck(3));
		boardState_.placeCard_hand_p1(new TheCoin());
		boardState_.setDeckPos_p1(4);
		
		GameRecord record = new GameSimpleRecord();
		record.put(0, s0_, (BoardState)boardState_.deepCopy());
		record.put(0, s1_, (BoardState)boardState_.flipPlayers().deepCopy());
				
		for (int i = 0; i < maxTurns_; ++i) {
			
			gms_[s0_].beginTurn(i, boardState_, players_[s0_], players_[s1_]);

			if (!boardState_.isAlive_p0()) {
				return new GameResult(s0_, s1_, i + 1, record);
			} else if (!boardState_.isAlive_p1()) {
				return new GameResult(s0_, s0_, i + 1, record);
			}

			boardState_ = gms_[s0_].playTurn(i, boardState_, players_[s0_], players_[s1_]);
			gms_[s0_].endTurn(i, boardState_, players_[s0_], players_[s1_]);

			record.put(i + 1, s0_, (BoardState)boardState_.deepCopy());
			if (!boardState_.isAlive_p0()) {
				return new GameResult(s0_, s1_, i + 1, record);
			} else if (!boardState_.isAlive_p1()) {
				return new GameResult(s0_, s0_, i + 1, record);
			}

			boardState_ = boardState_.flipPlayers();

			gms_[s1_].beginTurn(i, boardState_, players_[s1_], players_[s0_]);

			if (!boardState_.isAlive_p0()) {
				return new GameResult(s0_, s0_, i + 1, record);
			} else if (!boardState_.isAlive_p1()) {
				return new GameResult(s0_, s1_, i + 1, record);
			}

			boardState_ = gms_[s1_].playTurn(i, boardState_, players_[s1_], players_[s0_]);
			gms_[s1_].endTurn(i, boardState_, players_[s1_], players_[s0_]);
			record.put(i + 1, s1_, (BoardState)boardState_.deepCopy());

			if (!boardState_.isAlive_p0()) {
				return new GameResult(s0_, s0_, i + 1, record);
			} else if (!boardState_.isAlive_p1()) {
				return new GameResult(s0_, s1_, i + 1, record);
			}

			boardState_ = boardState_.flipPlayers();
			
		}
		return new GameResult(s0_, -1, 0, record);
	}
}
