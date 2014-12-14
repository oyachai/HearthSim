package com.hearthsim.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.hearthsim.Game;
import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.ImplementedCardList;
import com.hearthsim.card.ImplementedCardList.ImplementedCard;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.NoviceEngineer;
import com.hearthsim.card.minion.heroes.Mage;
import com.hearthsim.card.minion.heroes.Paladin;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.card.spellcard.concrete.AnimalCompanion;
import com.hearthsim.card.spellcard.concrete.ArcaneIntellect;
import com.hearthsim.card.spellcard.concrete.Soulfire;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.results.GameResult;
import com.hearthsim.util.HearthActionBoardPair;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestGame {
	private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

	@Test
	public void testGameTaunt30() {

		int numCardsInDeck_ = 30;
		byte minionAttack = 5;
		byte minionHealth = 4;
		byte minionMana = 4;

		int numTaunts_ = 30;

		Card[] cards1_ = new Card[numCardsInDeck_];
		Card[] cards2_ = new Card[numCardsInDeck_];

		for(int i = 0; i < numCardsInDeck_; ++i) {
			byte attack = minionAttack;
			byte health = minionHealth;
			byte mana = minionMana;
			cards1_[i] = new Minion("" + i, mana, attack, health, attack, health, health);
			cards2_[i] = new Minion("" + i, mana, attack, health, attack, health, health);
		}

		int nt = 0;
		while(nt < numTaunts_) {
			int irand = (int)(Math.random() * numCardsInDeck_);
			if(!((Minion)cards1_[irand]).getTaunt()) {
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

		PlayerModel playerModel1 = new PlayerModel((byte)0, "player0", hero1, deck1);
		PlayerModel playerModel2 = new PlayerModel((byte)1, "player1", hero2, deck2);

		BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();

		BruteForceSearchAI ai1 = BruteForceSearchAI.buildStandardAI1();

		long t1 = System.nanoTime();
		Game game = new Game(playerModel1, playerModel2, ai0, ai1, false);
		GameResult result = null;
		try {
			result = game.runGame();
		} catch(HSException e) {
			result = new GameResult(0, -1, 0, null);
		}

		long t2 = System.nanoTime();

		log.info("f = " + result.firstPlayerIndex_ + ", w = " + result.winnerPlayerIndex_ + ", time taken = "
				+ (t2 - t1) / 1000000.0 + " ms");

		assertEquals(result.winnerPlayerIndex_, 0);
	}

	@Test
	public void testGameHistoryIsRepeatable() throws HSException {

		int numCardsInDeck_ = 12;
		byte minionAttack = 5;
		byte minionHealth = 4;
		byte minionMana = 4;

		Card[] cards1_ = new Card[numCardsInDeck_];
		Card[] cards2_ = new Card[numCardsInDeck_];

		for(int i = 0; i < numCardsInDeck_; ++i) {
			byte attack = minionAttack;
			byte health = minionHealth;
			byte mana = minionMana;
			cards1_[i] = new Minion("" + i, mana, attack, health, attack, health, health);
			cards2_[i] = new Minion("" + i, mana, attack, health, attack, health, health);
		}

		Deck deck1 = new Deck(cards1_);
		Deck deck2 = new Deck(cards2_);

		deck1.shuffle();
		deck2.shuffle();

		PlayerModel playerModel1 = new PlayerModel((byte)0, "player0", new Paladin(), deck1);
		PlayerModel playerModel2 = new PlayerModel((byte)1, "player1", new Mage(), deck2);

		BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
		BruteForceSearchAI ai1 = BruteForceSearchAI.buildStandardAI1();

		Game game = new Game(playerModel1, playerModel2, ai0, ai1, false);
		game.runGame();
		List<HearthActionBoardPair> history = game.gameHistory;
		this.assertActionTreeIsRepeatable(history, deck1, deck2);
	}

	@Test
	public void testGameHistoryIsRepeatableWithRng() throws HSException {

		int numCardsInDeck_ = 12;
		byte minionAttack = 5;
		byte minionHealth = 4;
		byte minionMana = 4;

		ArrayList<Card> cards1_ = new ArrayList<Card>();
		ArrayList<Card> cards2_ = new ArrayList<Card>();

		cards1_.add(new AnimalCompanion());
		cards2_.add(new AnimalCompanion());

		cards1_.add(new Soulfire());
		cards2_.add(new Soulfire());

		for(int i = cards1_.size(); i < numCardsInDeck_; ++i) {
			byte attack = minionAttack;
			byte health = minionHealth;
			byte mana = minionMana;
			cards1_.add(new Minion("" + i, mana, attack, health, attack, health, health));
			cards2_.add(new Minion("" + i, mana, attack, health, attack, health, health));
		}

		Deck deck1 = new Deck(cards1_);
		Deck deck2 = new Deck(cards2_);

		//deck1.shuffle();
		//deck2.shuffle();

		PlayerModel playerModel1 = new PlayerModel((byte)0, "player0", new Paladin(), deck1);
		PlayerModel playerModel2 = new PlayerModel((byte)1, "player1", new Mage(), deck2);

		BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
		BruteForceSearchAI ai1 = BruteForceSearchAI.buildStandardAI1();

		Game game = new Game(playerModel1, playerModel2, ai0, ai1, false);
		game.runGame();
		List<HearthActionBoardPair> history = game.gameHistory;
		this.assertActionTreeIsRepeatable(history, deck1, deck2);
	}

	@Test
	public void testGameHistoryIsRepeatableWithCardDraw() throws HSException {

		int numCardsInDeck_ = 12;
		byte minionAttack = 5;
		byte minionHealth = 4;
		byte minionMana = 4;

		ArrayList<Card> cards1_ = new ArrayList<Card>();
		ArrayList<Card> cards2_ = new ArrayList<Card>();

		cards1_.add(new ArcaneIntellect());
		cards2_.add(new ArcaneIntellect());

		cards1_.add(new NoviceEngineer());
		cards2_.add(new NoviceEngineer());

		for(int i = cards1_.size(); i < numCardsInDeck_; ++i) {
			byte attack = minionAttack;
			byte health = minionHealth;
			byte mana = minionMana;
			cards1_.add(new Minion("" + i, mana, attack, health, attack, health, health));
			cards2_.add(new Minion("" + i, mana, attack, health, attack, health, health));
		}

		Deck deck1 = new Deck(cards1_);
		Deck deck2 = new Deck(cards2_);

		//deck1.shuffle();
		//deck2.shuffle();

		PlayerModel playerModel1 = new PlayerModel((byte)0, "player0", new Paladin(), deck1);
		PlayerModel playerModel2 = new PlayerModel((byte)1, "player1", new Mage(), deck2);

		BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
		BruteForceSearchAI ai1 = BruteForceSearchAI.buildStandardAI1();

		Game game = new Game(playerModel1, playerModel2, ai0, ai1, false);
		game.runGame();
		List<HearthActionBoardPair> history = game.gameHistory;
		this.assertActionTreeIsRepeatable(history, deck1, deck2);
	}

	@Test
	@Ignore("Long and uses randomized data")
	// This test is purely for hunting new bugs. It can take a while to run and should not be included in the Travis test run
	public void testGameHistoryWithRandomDecks() throws HSException {
		int numCardsInDeck_ = 30;

		ImplementedCardList supportedCards = ImplementedCardList.getInstance();
		ArrayList<ImplementedCard> allCards = supportedCards.getCardList();

		ArrayList<Card> cards1_ = new ArrayList<Card>();
		ArrayList<Card> cards2_ = new ArrayList<Card>();

		while(cards1_.size() < numCardsInDeck_) {
			int thisCardIndex = (int)Math.floor(Math.random() * (allCards.size() - 1));
			ImplementedCard card = allCards.get(thisCardIndex);

			if(card.isHero) continue;

			cards1_.add(card.createCardInstance());
		}

		while(cards2_.size() < numCardsInDeck_) {
			int thisCardIndex = (int)Math.floor(Math.random() * (allCards.size() - 1));
			ImplementedCard card = allCards.get(thisCardIndex);

			if(card.isHero) continue;

			cards2_.add(card.createCardInstance());
		}

		Deck deck1 = new Deck(cards1_);
		Deck deck2 = new Deck(cards2_);

		PlayerModel playerModel1 = new PlayerModel((byte)0, "player0", new TestHero(), deck1);
		PlayerModel playerModel2 = new PlayerModel((byte)1, "player1", new TestHero(), deck2);

		BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
		BruteForceSearchAI ai1 = BruteForceSearchAI.buildStandardAI1();

		Game game = new Game(playerModel1, playerModel2, ai0, ai1, false);
		game.runGame();
		List<HearthActionBoardPair> history = game.gameHistory;
		// TODO the output this generates is terrible and since the test is not repeatable it isn't too helpful right now
		this.assertActionTreeIsRepeatable(history, deck1, deck2);
	}

	@Test
	public void testGameRecord0() {

		// Whichever player that is using the 4 mana 5/4 minions should always win

		int numCardsInDeck_ = 30;
		byte minionAttack = 5;
		byte minionHealth = 4;
		byte minionMana = 4;

		Card[] cards1_ = new Card[numCardsInDeck_];
		Card[] cards2_ = new Card[numCardsInDeck_];

		for(int i = 0; i < numCardsInDeck_; ++i) {
			byte attack = minionAttack;
			byte health = minionHealth;
			byte mana = minionMana;
			cards1_[i] = new Minion("" + i, mana, attack, health, attack, health, health);
			cards2_[i] = new Minion("" + i, (byte)9, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1);
		}

		for(int iter = 0; iter < 10; ++iter) {
			long t1 = System.nanoTime();
			Hero hero1 = new TestHero();
			Hero hero2 = new TestHero();

			Deck deck1 = new Deck(cards1_);
			Deck deck2 = new Deck(cards2_);

			deck1.shuffle();
			deck2.shuffle();

			PlayerModel playerModel1 = new PlayerModel((byte)0, "player0", hero1, deck1);
			PlayerModel playerModel2 = new PlayerModel((byte)1, "player1", hero2, deck2);

			BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
			BruteForceSearchAI ai1 = BruteForceSearchAI.buildStandardAI1();

			Game game = new Game(playerModel1, playerModel2, ai0, ai1, true);
			GameResult result = null;
			try {
				result = game.runGame();
			} catch(HSException e) {
				result = new GameResult(0, -1, 0, null);
			}
			long t2 = System.nanoTime();

			log.info("f = " + result.firstPlayerIndex_ + ", w = " + result.winnerPlayerIndex_ + ", time taken = "
					+ (t2 - t1) / 1000000.0 + " ms");
			if(result.firstPlayerIndex_ == 0) {
				// Player0 went first, so he should have 3 cards on turn 0
				assertEquals(3, result.record_.getNumCardsInHand(0, 0, 0));
				assertEquals(5, result.record_.getNumCardsInHand(1, 0, 1));
			} else {
				// Player0 went second, so he should have 5 cards on turn 0
				assertEquals(5, result.record_.getNumCardsInHand(0, 0, 0));
				assertEquals(3, result.record_.getNumCardsInHand(1, 0, 1));
			}

			// Player0 should always win
			assertEquals("testGameRecord0 iteration " + iter, result.winnerPlayerIndex_, 0);
		}
	}

	private void assertActionTreeIsRepeatable(List<HearthActionBoardPair> history, Deck deck1, Deck deck2)
			throws HSException {
		HearthTreeNode current = null;
		for(HearthActionBoardPair actionBoardPair : history) {
			if(current == null) {
				current = new HearthTreeNode(actionBoardPair.board.deepCopy());
			} else {
				assertNotNull(current);

				if(actionBoardPair.action == null) {
					log.error("Node without action detected. Previous board state: {0}", current.data_.toJSON());
					log.error("Node without action detected. Expected board state: {0}", actionBoardPair.board.toJSON());

					assertNotNull(actionBoardPair.action);
				}
				current = actionBoardPair.action.perform(current, deck1, deck2, false);

				assertNotNull("Should have new node after " + actionBoardPair.action.verb_ + " action", current);
				if(!actionBoardPair.board.equals(current.data_)) {
					log.error("Detected history mismatch after action: {0}", actionBoardPair.action);
					assertEquals(actionBoardPair.board, current.data_);
				}
			}
		}
	}
}
