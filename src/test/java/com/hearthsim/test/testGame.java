package com.hearthsim.test;

import com.hearthsim.entity.BaseEntity;
import com.hearthsim.Game;
import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.heroes.Mage;
import com.hearthsim.card.minion.heroes.Paladin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.results.GameResult;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class testGame {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
	

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
			if (!((BaseEntity)cards1_[irand]).getTaunt()) {
				((BaseEntity)cards1_[irand]).setTaunt(true);
				++nt;
			}
		}
		

		Hero hero1 = new Paladin();
		Hero hero2 = new Mage();
		
		Deck deck1 = new Deck(cards1_);
		Deck deck2 = new Deck(cards2_);
		
		deck1.shuffle();
		deck2.shuffle();
		
		PlayerModel playerModel1 = new PlayerModel(0, "player0", hero1, deck1);
		PlayerModel playerModel2 = new PlayerModel(1, "player1", hero2, deck2);

        ArtificialPlayer ai0 = ArtificialPlayer.buildStandardAI1();

        ArtificialPlayer ai1 = ArtificialPlayer.buildStandardAI1();

		long t1 = System.nanoTime();
		Game game = new Game(playerModel1, playerModel2, ai0, ai1, false);
		GameResult w = null;
		try {
			w = game.runGame();
		} catch (HSException e) {
			w = new GameResult(0, -1, 0, null);
		}
		
		long t2 = System.nanoTime();
		
		log.info("f = " + w.firstPlayerIndex_ + ", w = " + w.winnerPlayerIndex_ + ", time taken = " + (t2 - t1) / 1000000.0 + " ms");
		
		assertTrue("testGame0", w.winnerPlayerIndex_ == 0);
	}

	@Test
	public void testGame1() {
		
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
			cards2_[i] = new Minion("" + i, (byte)9, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1);
		}


		for (int iter = 0; iter < 10; ++iter) {
			long t1 = System.nanoTime();
			Hero hero1 = new Hero();
			Hero hero2 = new Hero();
			
			Deck deck1 = new Deck(cards1_);
			Deck deck2 = new Deck(cards2_);
			
			deck1.shuffle();
			deck2.shuffle();
			
			PlayerModel playerModel1 = new PlayerModel(0, "player0", hero1, deck1);
			PlayerModel playerModel2 = new PlayerModel(1, "player1", hero2, deck2);

	        ArtificialPlayer ai0 = ArtificialPlayer.buildStandardAI1();
	        ArtificialPlayer ai1 = ArtificialPlayer.buildStandardAI1();

			Game game = new Game(playerModel1, playerModel2, ai0, ai1, true);
			GameResult w = null;
			try {
				w = game.runGame();
			} catch (HSException e) {
				w = new GameResult(0, -1, 0, null);
			}
			long t2 = System.nanoTime();
			log.info("f = " + w.firstPlayerIndex_ + ", w = " + w.winnerPlayerIndex_ + ", time taken = " + (t2 - t1) / 1000000.0 + " ms");
			assertTrue("testGame0", w.winnerPlayerIndex_ == 0);
		}		
	}
}
