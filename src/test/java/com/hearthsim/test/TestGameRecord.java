package com.hearthsim.test;

import com.hearthsim.Game;
import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.results.GameResult;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestGameRecord {

	private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

	@Test
	public void testGameRecord0() {

		// Whichever player that is using the 4 mana 5/4 minions should always win

		int numCardsInDeck_ = 30;
		byte minionAttack = 5;
		byte minionHealth = 4;
		byte minionMana = 4;

		Card[] cards1_ = new Card[numCardsInDeck_];
		Card[] cards2_ = new Card[numCardsInDeck_];

		for (int i = 0; i < numCardsInDeck_; ++i) {
			byte attack = minionAttack;
			byte health = minionHealth;
			byte mana = minionMana;
			cards1_[i] = new Minion("" + i, mana, attack, health, attack, health, health);
			cards2_[i] = new Minion("" + i, (byte) 9, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1);
		}

		for (int iter = 0; iter < 10; ++iter) {
			long t1 = System.nanoTime();
			Hero hero1 = new TestHero();
			Hero hero2 = new TestHero();

			Deck deck1 = new Deck(cards1_);
			Deck deck2 = new Deck(cards2_);

			deck1.shuffle();
			deck2.shuffle();

			PlayerModel playerModel1 = new PlayerModel(0, "player0", hero1, deck1);
			PlayerModel playerModel2 = new PlayerModel(1, "player1", hero2, deck2);

			BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
			BruteForceSearchAI ai1 = BruteForceSearchAI.buildStandardAI1();

			Game game = new Game(playerModel1, playerModel2, ai0, ai1, true);
			GameResult w = null;
			try {
				w = game.runGame();
			} catch (HSException e) {
				w = new GameResult(0, -1, 0, null);
			}
			long t2 = System.nanoTime();

			log.info("f = " + w.firstPlayerIndex_ + ", w = " + w.winnerPlayerIndex_ + ", time taken = " + (t2 - t1)
					/ 1000000.0 + " ms");
			if (w.firstPlayerIndex_ == 0) {
				// Player0 went first, so he should have 3 cards on turn 0
				assertEquals(3, w.record_.getNumCardsInHand(0, 0, 0));
				assertEquals(5, w.record_.getNumCardsInHand(1, 0, 1));
			} else {
				// Player0 went second, so he should have 5 cards on turn 0
				assertEquals(5, w.record_.getNumCardsInHand(0, 0, 0));
				assertEquals(3, w.record_.getNumCardsInHand(1, 0, 1));
			}

			// Player0 should always win
			assertTrue("testGame0", w.winnerPlayerIndex_ == 0);
		}
	}
}
