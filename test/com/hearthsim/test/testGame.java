package com.hearthsim.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.hearthsim.Game;
import com.hearthsim.GameResult;
import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.heroes.*;
import com.hearthsim.exception.HSException;
import com.hearthsim.player.Player;
import com.hearthsim.player.playercontroller.ArtificialPlayer;

public class testGame {
	

	@Test
	public void testGame0() {
		
		int numCardsInDeck_ = 30;
		byte minionAttack = 3;
		byte minionHealth = 4;
		byte minionMana = 3;
		
		int numTaunts_ = 30;
		
		Card[] cards1_ = new Card[numCardsInDeck_];
		Card[] cards2_ = new Card[numCardsInDeck_];

		for (int i = 0; i < numCardsInDeck_; ++i) {
			byte attack = minionAttack;
			byte health = minionHealth;
			byte mana = minionMana;
			cards1_[i] = new Minion("" + i, mana, attack, health, attack, health, health, false, false, false, false, false, false, false, false, false, true, false);
			cards2_[i] = new Minion("" + i, mana, attack, health, attack, health, health, false, false, false, false, false, false, false, false, false, true, false);
		}
		
		int nt = 0;
		while (nt < numTaunts_) {
			int irand = (int)(Math.random() * numCardsInDeck_);
			if (!((Minion)cards1_[irand]).getTaunt()) {
				((Minion)cards1_[irand]).setTaunt(true);
				++nt;
			}
		}
		

		Hero hero1 = new Rogue();
		Hero hero2 = new Mage();
		
		Deck deck1 = new Deck(cards1_);
		Deck deck2 = new Deck(cards2_);
		
		deck1.shuffle();
		deck2.shuffle();
		
		Player player1 = new Player("player0", hero1, deck1);
		Player player2 = new Player("player1", hero2, deck2);
		
		ArtificialPlayer ai0 = new ArtificialPlayer(
				0.9,
				0.9,
				1.0,
				1.0,
				1.0,
				0.1,
				0.1,
				0.1,
				0.5,
				0.5,
				0.0,
				0.5,
				0.0,
				0.0
				);
		
		ArtificialPlayer ai1 = new ArtificialPlayer(
				0.9,
				0.9,
				1.0,
				1.0,
				1.0,
				0.1,
				0.1,
				0.1,
				0.5,
				0.5,
				0.0,
				0.5,
				0.0,
				0.0
				);

		Game game = new Game(player1, player2, ai0, ai1);
		GameResult w = null;
		try {
			w = game.runGame();
		} catch (HSException e) {
			w = new GameResult(-1, 0);
		}
		
		System.out.println("w = " + w.winnerPlayerIndex_);
		
		assertTrue("testGame0", w.winnerPlayerIndex_ == 0);
	}
}
