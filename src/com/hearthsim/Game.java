package com.hearthsim;

import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSPlayer0WinsException;
import com.hearthsim.exception.HSPlayer1WinsException;
import com.hearthsim.player.Player;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.player.playercontroller.GameMaster;
import com.hearthsim.util.BoardState;

public class Game {

	final static int maxTurns_ = 100;
	
	BoardState[] boards_;
	Player[] players_;
	GameMaster[] gms_;
	
	int cur_turn_;
	
	public Game(Player player0, Player player1, ArtificialPlayer ai0, ArtificialPlayer ai1) {
		players_ = new Player[2];
		players_[0] = player0;
		players_[1] = player1;
		boards_ = new BoardState[2];
		boards_[0] = new BoardState();
		boards_[1] = new BoardState();
		gms_ = new GameMaster[2];
		gms_[0] = new GameMaster(ai0);
		gms_[1] = new GameMaster(ai1);
	}
		
	public BoardState getBoardState(int playerID) {
		return boards_[playerID];
	}
	
	public int runGame() throws HSException {
		cur_turn_ = 0;
				
		//the first player draws 3 cards
		boards_[0].placeCard_hand(players_[0].drawFromDeck(0));
		boards_[0].placeCard_hand(players_[0].drawFromDeck(1));
		boards_[0].placeCard_hand(players_[0].drawFromDeck(2));
		boards_[0].setDeckPos(3);

		//the second player draws 4 cards
		boards_[1].placeCard_hand(players_[1].drawFromDeck(0));
		boards_[1].placeCard_hand(players_[1].drawFromDeck(1));
		boards_[1].placeCard_hand(players_[1].drawFromDeck(2));
		boards_[1].placeCard_hand(players_[1].drawFromDeck(3));
		boards_[1].placeCard_hand(new TheCoin());
		boards_[1].setDeckPos(4);
		
		for (int i = 0; i < maxTurns_; ++i) {
			
			gms_[0].beginTurn(i, boards_[0], players_[0]);

			if (!boards_[0].isAlive_p0()) {
				//player 0 is dead!
				System.out.println("player 1 wins");
				return 1;
			} else if (!boards_[0].isAlive_p1()) {
				System.out.println("player 0 wins");
				return 0;
			}

			boards_[0] = gms_[0].playTurn(i, boards_[0], players_[0]);
			gms_[0].endTurn(i, boards_[0], players_[0]);

			if (!boards_[0].isAlive_p0()) {
				//player 0 is dead!
				System.out.println("player 1 wins");
				return 1;
			} else if (!boards_[0].isAlive_p1()) {
				System.out.println("player 0 wins");
				return 0;
			}

			boards_[1] = boards_[0].flipPlayers(boards_[1].getCards_hand(), boards_[1].getDeckPos());

			gms_[1].beginTurn(i, boards_[1], players_[1]);

			if (!boards_[1].isAlive_p0()) {
				//player 0 is dead!
				System.out.println("player 0 wins");
				return 0;
			} else if (!boards_[1].isAlive_p1()) {
				System.out.println("player 1 wins");
				return 1;
			}

			boards_[1] = gms_[1].playTurn(i, boards_[1], players_[1]);
			gms_[1].endTurn(i, boards_[1], players_[1]);

			boards_[0] = boards_[1].flipPlayers(boards_[0].getCards_hand(), boards_[0].getDeckPos());
			
			if (!boards_[0].isAlive_p0()) {
				//player 0 is dead!
				System.out.println("player 1 wins");
				return 1;
			} else if (!boards_[0].isAlive_p1()) {
				System.out.println("player 0 wins");
				return 0;
			}
		}
		return -1;
	}
}
