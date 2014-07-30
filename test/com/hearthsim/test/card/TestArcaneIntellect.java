package com.hearthsim.test.card;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.spellcard.concrete.ArcaneIntellect;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.player.Player;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestArcaneIntellect {

	private HearthTreeNode board;
	private static final byte mana = 2;
	private static final byte attack0 = 2;
	private static final byte health0 = 5;
	private static final byte health1 = 1;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardState());
		
		Minion minion0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion1 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion2 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1);
		Minion minion3 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);

		ArcaneIntellect fb = new ArcaneIntellect();
		board.data_.placeCard_hand_p0(fb);
		board.data_.placeMinion(0, minion0);
		board.data_.placeMinion(1, minion1);
		board.data_.placeMinion(1, minion2);
		board.data_.placeMinion(1, minion3);
		
		board.data_.setMana_p0(5);
	}
	
	@Test
	public void test0() throws HSInvalidPlayerIndexException {

		
		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}
		
		Deck deck = new Deck(cards);
		
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode res;
		Minion target = null;
		
		target = board.data_.getCharacter(1, 0);
		res = theCard.useOn(1, target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(0, 1);
		res = theCard.useOn(0, target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(1, 1);
		res = theCard.useOn(1, target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(1, 2);
		res = theCard.useOn(1, target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(1, 3);
		res = theCard.useOn(1, target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(0, 0);
		res = theCard.useOn(0, target, board, deck, null);
		assertFalse(res == null);
		assertEquals(res.data_.getNumCards_hand(), 0);
		assertTrue(res instanceof CardDrawNode);
		assertEquals( ((CardDrawNode)res).getNumCardsToDraw(), 2);
		
		assertTrue(res.data_.getNumMinions_p0() == 1);
		assertTrue(res.data_.getNumMinions_p1() == 3);
		assertTrue(res.data_.getMana_p0() == 2);
		assertTrue(res.data_.getMinion_p0(0).getHealth() == health0);
		assertTrue(res.data_.getMinion_p0(0).getAttack() == attack0);
		assertTrue(res.data_.getMinion_p1(0).getHealth() == health0);
		assertTrue(res.data_.getMinion_p1(0).getAttack() == attack0);
		assertTrue(res.data_.getMinion_p1(1).getHealth() == health1);
		assertTrue(res.data_.getMinion_p1(1).getAttack() == attack0);
		assertTrue(res.data_.getMinion_p1(2).getHealth() == health0);
		assertTrue(res.data_.getMinion_p1(2).getAttack() == attack0);
		assertTrue(res.data_.getHero_p0().getHealth() == 30);
		assertTrue(res.data_.getHero_p1().getHealth() == 30);

	}

	@Test
	public void test1() throws HSInvalidPlayerIndexException {
		
		Card cards[] = new Card[1];
		for (int index = 0; index < 1; ++index) {
			cards[index] = new TheCoin();
		}
		
		Deck deck = new Deck(cards);
		
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode res;
		Minion target = null;
		
		target = board.data_.getCharacter(1, 0);
		res = theCard.useOn(1, target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(0, 1);
		res = theCard.useOn(0, target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(1, 1);
		res = theCard.useOn(1, target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(1, 2);
		res = theCard.useOn(1, target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(1, 3);
		res = theCard.useOn(1, target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(0, 0);
		res = theCard.useOn(0, target, board, deck, null);
		assertFalse(res == null);
		assertEquals(res.data_.getNumCards_hand(), 0);
		assertTrue(res instanceof CardDrawNode);
		assertEquals( ((CardDrawNode)res).getNumCardsToDraw(), 2);

		assertTrue(res.data_.getNumMinions_p0() == 1);
		assertTrue(res.data_.getNumMinions_p1() == 3);
		assertTrue(res.data_.getMana_p0() == 2);
		assertTrue(res.data_.getMinion_p0(0).getHealth() == health0);
		assertTrue(res.data_.getMinion_p0(0).getAttack() == attack0);
		assertTrue(res.data_.getMinion_p1(0).getHealth() == health0);
		assertTrue(res.data_.getMinion_p1(0).getAttack() == attack0);
		assertTrue(res.data_.getMinion_p1(1).getHealth() == health1);
		assertTrue(res.data_.getMinion_p1(1).getAttack() == attack0);
		assertTrue(res.data_.getMinion_p1(2).getHealth() == health0);
		assertTrue(res.data_.getMinion_p1(2).getAttack() == attack0);

	}

	@Test
	public void test2() throws HSException {
		
		int numCards = 10;
		Card cards[] = new Card[numCards];
		for (int index = 0; index < numCards; ++index) {
			cards[index] = new BloodfenRaptor();
		}
		
		Deck deck = new Deck(cards);
		
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
		
		Hero hero = new Hero();		
		Player player0 = new Player("player0", hero, deck);
		Player player1 = new Player("player0", hero, deck);
		
		board.data_.setMana_p0((byte)3);
		board.data_.setMana_p1((byte)3);

		board.data_.setMaxMana_p0((byte)3);
		board.data_.setMaxMana_p1((byte)3);

		BoardState resBoard = ai0.playTurn(0, board.data_, player0, player1);
		
		assertFalse( resBoard == null );
		
		assertEquals( resBoard.getMana_p0(), 0 );
		assertEquals( resBoard.getMana_p1(), 3 );
		assertEquals( resBoard.getNumCards_hand_p0(), 2 );
		assertEquals( resBoard.getNumMinions_p0(), 1 );
		assertEquals( resBoard.getNumMinions_p1(), 2 );
	}
	
	@Test
	public void test3() throws HSException {
		
		int numCards = 10;
		Card cards[] = new Card[numCards];
		for (int index = 0; index < numCards; ++index) {
			cards[index] = new BloodfenRaptor();
		}
		
		Deck deck = new Deck(cards);
		
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
		
		Hero hero = new Hero();		
		Player player0 = new Player("player0", hero, deck);
		Player player1 = new Player("player0", hero, deck);
		
		board.data_.setMana_p0((byte)6);
		board.data_.setMana_p1((byte)6);

		board.data_.setMaxMana_p0((byte)6);
		board.data_.setMaxMana_p1((byte)6);

		BoardState resBoard = ai0.playTurn(0, board.data_, player0, player1);
		
		assertFalse( resBoard == null );
		
		assertEquals( resBoard.getMana_p0(), 1 );
		assertEquals( resBoard.getMana_p1(), 6 );
		assertEquals( resBoard.getNumCards_hand_p0(), 1 );
		assertEquals( resBoard.getNumMinions_p0(), 2 );
		assertEquals( resBoard.getNumMinions_p1(), 2 );
	}
	
	@Test
	public void test4() throws HSException {
		
		int numCards = 10;
		Card cards[] = new Card[numCards];
		for (int index = 0; index < numCards; ++index) {
			cards[index] = new BloodfenRaptor();
		}
		
		Deck deck = new Deck(cards);
		
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
		
		Hero hero = new Hero();		
		Player player0 = new Player("player0", hero, deck);
		Player player1 = new Player("player0", hero, deck);
		
		board.data_.setMana_p0((byte)9);
		board.data_.setMana_p1((byte)9);

		board.data_.setMaxMana_p0((byte)9);
		board.data_.setMaxMana_p1((byte)9);

		BoardState resBoard = ai0.playTurn(0, board.data_, player0, player1);
		
		assertFalse( resBoard == null );
		
		assertEquals( resBoard.getMana_p0(), 2 );
		assertEquals( resBoard.getMana_p1(), 9 );
		assertEquals( resBoard.getNumCards_hand_p0(), 0 );
		assertEquals( resBoard.getNumMinions_p0(), 3 );
		assertEquals( resBoard.getNumMinions_p1(), 2 );
	}
}
