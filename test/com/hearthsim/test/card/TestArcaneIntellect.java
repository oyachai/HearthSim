package com.hearthsim.test.card;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.concrete.ArcaneIntellect;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.util.BoardState;

public class TestArcaneIntellect {

	private BoardState board;
	private static final byte mana = 2;
	private static final byte attack0 = 2;
	private static final byte health0 = 5;
	private static final byte health1 = 1;

	@Before
	public void setup() {
		board = new BoardState();
	}
	
	@Test
	public void test0() {
		Minion minion0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion1 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion2 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1);
		Minion minion3 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);

		ArcaneIntellect fb = new ArcaneIntellect();
		board.placeCard_hand(fb);
		board.placeMinion_p0(minion0);
		board.placeMinion_p1(minion1);
		board.placeMinion_p1(minion2);
		board.placeMinion_p1(minion3);
		
		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}
		
		Deck deck = new Deck(cards);
		
		Card theCard = board.getCard_hand(0);
		BoardState res;
		
		res = theCard.useOn(0, 1, 0, board, deck);
		assertTrue(res == null);
		
		res = theCard.useOn(0, 0, 1, board, deck);
		assertTrue(res == null);
		
		res = theCard.useOn(0, 1, 1, board, deck);
		assertTrue(res == null);
		
		res = theCard.useOn(0, 1, 2, board, deck);
		assertTrue(res == null);
		
		res = theCard.useOn(0, 1, 3, board, deck);
		assertTrue(res == null);
		
		res = theCard.useOn(0, 0, 0, board, deck);
		assertFalse(res == null);
		assertTrue(res.getNumCards_hand() == 2);
		assertTrue(res.getNumMinions_p0() == 1);
		assertTrue(res.getNumMinions_p1() == 3);
		assertTrue(res.getMinion_p0(0).getHealth() == health0);
		assertTrue(res.getMinion_p0(0).getAttack() == attack0);
		assertTrue(res.getMinion_p1(0).getHealth() == health0);
		assertTrue(res.getMinion_p1(0).getAttack() == attack0);
		assertTrue(res.getMinion_p1(1).getHealth() == health1);
		assertTrue(res.getMinion_p1(1).getAttack() == attack0);
		assertTrue(res.getMinion_p1(2).getHealth() == health0);
		assertTrue(res.getMinion_p1(2).getAttack() == attack0);
		assertTrue(res.getHero_p0().getHealth() == 30);
		assertTrue(res.getHero_p1().getHealth() == 30);

	}

	@Test
	public void test1() {
		Minion minion0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion1 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion2 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1);
		Minion minion3 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);

		ArcaneIntellect fb = new ArcaneIntellect();
		board.placeCard_hand(fb);
		board.placeMinion_p0(minion0);
		board.placeMinion_p1(minion1);
		board.placeMinion_p1(minion2);
		board.placeMinion_p1(minion3);
		
		Card cards[] = new Card[1];
		for (int index = 0; index < 1; ++index) {
			cards[index] = new TheCoin();
		}
		
		Deck deck = new Deck(cards);
		
		Card theCard = board.getCard_hand(0);
		BoardState res;
		
		res = theCard.useOn(0, 1, 0, board, deck);
		assertTrue(res == null);
		
		res = theCard.useOn(0, 0, 1, board, deck);
		assertTrue(res == null);
		
		res = theCard.useOn(0, 1, 1, board, deck);
		assertTrue(res == null);
		
		res = theCard.useOn(0, 1, 2, board, deck);
		assertTrue(res == null);
		
		res = theCard.useOn(0, 1, 3, board, deck);
		assertTrue(res == null);
		
		res = theCard.useOn(0, 0, 0, board, deck);
		assertFalse(res == null);
		assertTrue(res.getNumCards_hand() == 1);
		assertTrue(res.getNumMinions_p0() == 1);
		assertTrue(res.getNumMinions_p1() == 3);
		assertTrue(res.getMinion_p0(0).getHealth() == health0);
		assertTrue(res.getMinion_p0(0).getAttack() == attack0);
		assertTrue(res.getMinion_p1(0).getHealth() == health0);
		assertTrue(res.getMinion_p1(0).getAttack() == attack0);
		assertTrue(res.getMinion_p1(1).getHealth() == health1);
		assertTrue(res.getMinion_p1(1).getAttack() == attack0);
		assertTrue(res.getMinion_p1(2).getHealth() == health0);
		assertTrue(res.getMinion_p1(2).getAttack() == attack0);
		assertTrue(res.getHero_p0().getHealth() == 29);
		assertTrue(res.getHero_p1().getHealth() == 30);

	}

	@Test
	public void test2() {
		Minion minion0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion1 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion2 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1);
		Minion minion3 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);

		ArcaneIntellect fb = new ArcaneIntellect();
		board.placeCard_hand(fb);
		board.placeMinion_p0(minion0);
		board.placeMinion_p1(minion1);
		board.placeMinion_p1(minion2);
		board.placeMinion_p1(minion3);
		
		Card cards[] = new Card[0];
		
		Deck deck = new Deck(cards);
		
		Card theCard = board.getCard_hand(0);
		BoardState res;
		
		res = theCard.useOn(0, 1, 0, board, deck);
		assertTrue(res == null);
		
		res = theCard.useOn(0, 0, 1, board, deck);
		assertTrue(res == null);
		
		res = theCard.useOn(0, 1, 1, board, deck);
		assertTrue(res == null);
		
		res = theCard.useOn(0, 1, 2, board, deck);
		assertTrue(res == null);
		
		res = theCard.useOn(0, 1, 3, board, deck);
		assertTrue(res == null);
		
		res = theCard.useOn(0, 0, 0, board, deck);
		assertFalse(res == null);
		assertTrue(res.getNumCards_hand() == 0);
		assertTrue(res.getNumMinions_p0() == 1);
		assertTrue(res.getNumMinions_p1() == 3);
		assertTrue(res.getMinion_p0(0).getHealth() == health0);
		assertTrue(res.getMinion_p0(0).getAttack() == attack0);
		assertTrue(res.getMinion_p1(0).getHealth() == health0);
		assertTrue(res.getMinion_p1(0).getAttack() == attack0);
		assertTrue(res.getMinion_p1(1).getHealth() == health1);
		assertTrue(res.getMinion_p1(1).getAttack() == attack0);
		assertTrue(res.getMinion_p1(2).getHealth() == health0);
		assertTrue(res.getMinion_p1(2).getAttack() == attack0);
		assertTrue(res.getHero_p0().getHealth() == 27);
		assertTrue(res.getHero_p1().getHealth() == 30);

	}
}
