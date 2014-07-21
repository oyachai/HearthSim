package com.hearthsim;

import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.player.Player;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.player.playercontroller.GameMaster;
import com.hearthsim.util.BoardState;

public class Game {

	final static int maxTurns_ = 100;
	
	BoardState boardState_;
	Player[] players_;
	GameMaster[] gms_;

	int curPlayer_;
	int curTurn_;
	
	public Game(Player player0, Player player1, ArtificialPlayer ai0, ArtificialPlayer ai1) {
		players_ = new Player[2];
		players_[0] = player0;
		players_[1] = player1;
		boardState_ = new BoardState(player0.hero_, player1.hero_);
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
		boardState_.placeCard_hand_p0(players_[0].drawFromDeck(0));
		boardState_.placeCard_hand_p0(players_[0].drawFromDeck(1));
		boardState_.placeCard_hand_p0(players_[0].drawFromDeck(2));
		boardState_.setDeckPos_p0(3);

		//the second player draws 4 cards
		boardState_.placeCard_hand_p1(players_[1].drawFromDeck(0));
		boardState_.placeCard_hand_p1(players_[1].drawFromDeck(1));
		boardState_.placeCard_hand_p1(players_[1].drawFromDeck(2));
		boardState_.placeCard_hand_p1(players_[1].drawFromDeck(3));
		boardState_.placeCard_hand_p1(new TheCoin());
		boardState_.setDeckPos_p1(4);
		
		for (int i = 0; i < maxTurns_; ++i) {
			
			gms_[0].beginTurn(i, boardState_, players_[0]);

			if (!boardState_.isAlive_p0()) {
				return new GameResult(1, i + 1);
			} else if (!boardState_.isAlive_p1()) {
				return new GameResult(0, i + 1);
			}

			boardState_ = gms_[0].playTurn(i, boardState_, players_[0]);
			gms_[0].endTurn(i, boardState_, players_[0]);

			if (!boardState_.isAlive_p0()) {
				return new GameResult(1, i + 1);
			} else if (!boardState_.isAlive_p1()) {
				return new GameResult(0, i + 1);
			}

			boardState_ = boardState_.flipPlayers();

			gms_[1].beginTurn(i, boardState_, players_[1]);

			if (!boardState_.isAlive_p0()) {
				return new GameResult(0, i + 1);
			} else if (!boardState_.isAlive_p1()) {
				return new GameResult(1, i + 1);
			}

			boardState_ = gms_[1].playTurn(i, boardState_, players_[1]);
			gms_[1].endTurn(i, boardState_, players_[1]);

			boardState_ = boardState_.flipPlayers();
			
			if (!boardState_.isAlive_p0()) {
				return new GameResult(1, i + 1);
			} else if (!boardState_.isAlive_p1()) {
				return new GameResult(0, i + 1);
			}
		}
		return new GameResult(-1, 0);
	}
}
