package com.hearthsim.test;

import com.hearthsim.Game;
import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.heroes.Mage;
import com.hearthsim.card.minion.heroes.Paladin;
import com.hearthsim.exception.HSException;
import com.hearthsim.player.Player;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.results.GameResult;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class testGame {
	

	@Test
	public void testGame0() {
		
		int numCardsInDeck_ = 30;
		byte minionAttack = 5;
		byte minionHealth = 4;
		byte minionMana = 4;
		
		int numTaunts_ = 30;
		
		Card[] cards1_ = new Card[numCardsInDeck_];
		Card[] cards2_ = new Card[numCardsInDeck_];

		for (int i = 0; i < numCardsInDeck_; ++i) {
			byte attack = minionAttack;
			byte health = minionHealth;
			byte mana = minionMana;
			cards1_[i] = new Minion("" + i, mana, attack, health, attack, health, health);
			cards2_[i] = new Minion("" + i, mana, attack, health, attack, health, health);
		}
		
		int nt = 0;
		while (nt < numTaunts_) {
			int irand = (int)(Math.random() * numCardsInDeck_);
			if (!((Minion)cards1_[irand]).getTaunt()) {
				((Minion)cards1_[irand]).setTaunt(true);
				++nt;
			}
		}
		

		Hero hero1 = new Paladin();
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

		long t1 = System.nanoTime();
		Game game = new Game(player1, player2, ai0, ai1, false);
		GameResult w = null;
		try {
			w = game.runGame();
		} catch (HSException e) {
			w = new GameResult(0, -1, 0, null);
		}
		
		long t2 = System.nanoTime();
		
		System.out.println("f = " + w.firstPlayerIndex_ + ", w = " + w.winnerPlayerIndex_ + ", time taken = " + (t2 - t1) / 1000000.0 + " ms");
		
		assertTrue("testGame0", w.winnerPlayerIndex_ == 0);
	}
}
