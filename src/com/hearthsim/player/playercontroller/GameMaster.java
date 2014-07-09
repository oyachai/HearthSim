package com.hearthsim.player.playercontroller;

import java.util.Iterator;
import java.util.List;

import com.hearthsim.util.BoardState;
import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.player.Player;

public class GameMaster {
	
	ArtificialPlayer ai_;
	
	
	public GameMaster() {
		this(new ArtificialPlayer());
	}
	
	public GameMaster(ArtificialPlayer ai) {
		ai_ = ai;
	}

	public void initialize(BoardState board, Player player) throws HSException {
		
	}
	
	public void beginTurn(int turn, BoardState board, Player player) throws HSException
	{
		board.startTurn(player.getDeck());

		Card newCard = player.drawFromDeck(board.getDeckPos_p0());
		if (newCard == null) {
			//fatigue
			byte fatigueDamage = board.getFatigueDamage_p0();
			board.setFatigueDamage_p0((byte)(fatigueDamage + 1));
			board.getHero_p0().setHealth((byte)(board.getHero_p0().getHealth() - fatigueDamage));
		} else {
			board.setDeckPos_p0(board.getDeckPos_p0() + 1);
			board.placeCard_hand_p0(newCard);
		}
		if (board.getMana_p0() < 10)
			board.addMaxMana_p0(1);
		board.resetMana();
		
	}
	
	public BoardState playTurn(int turn, BoardState board, Player player) throws HSException {
		return ai_.playTurn(turn, board, player);
	}

	public void endTurn(int turn, BoardState board, Player player) throws HSException {
		board.endTurn(player.getDeck());
	}

}