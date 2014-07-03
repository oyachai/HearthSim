package com.hearthsim.test.card;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.concrete.AncestralHealing;
import com.hearthsim.util.BoardState;

public class TestAncestralHealing {
	
	private BoardState board;
	private static final byte mana = 2;
	private static final byte attack0 = 2;
	private static final byte health0 = 5;

	@Before
	public void setup() {
		board = new BoardState();
	}
	
	
	@Test
	public void test0() {
		Minion minion0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion1 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);

		AncestralHealing fb = new AncestralHealing();
		board.placeCard_hand(fb);
		board.placeMinion_p0(minion0);
		board.placeMinion_p1(minion1);
		
		Card theCard = board.getCard_hand(0);
		BoardState res;
		
		res = theCard.useOn(0, 0, 0, board);
		assertTrue(res == null);
		
		res = theCard.useOn(0, 1, 0, board);
		assertTrue(res == null);
		
	}

	@Test
	public void test1() {
		Minion minion0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion1 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);

		AncestralHealing fb = new AncestralHealing();
		board.placeCard_hand(fb);
		board.placeMinion_p0(minion0);
		board.placeMinion_p1(minion1);
		
		Card theCard = board.getCard_hand(0);
		BoardState res;
		
		res = theCard.useOn(0, 0, 1, board);
		assertFalse(res == null);
		assertTrue(res.getNumCards_hand() == 0);
		assertTrue(res.getMinion_p0(0).getHealth() == health0);
		assertTrue(res.getMinion_p0(0).getAttack() == attack0);
		assertTrue(res.getMinion_p0(0).getMaxHealth() == health0);
		assertTrue(res.getMinion_p0(0).getTaunt());
	}

	@Test
	public void test2() {
		Minion minion0 = new Minion("" + 0, mana, attack0, (byte)(health0-2), attack0, health0, health0);
		Minion minion1 = new Minion("" + 0, mana, attack0, (byte)(health0-2), attack0, health0, health0);

		AncestralHealing fb = new AncestralHealing();
		board.placeCard_hand(fb);
		board.placeMinion_p0(minion0);
		board.placeMinion_p1(minion1);
		
		Card theCard = board.getCard_hand(0);
		BoardState res;
		
		res = theCard.useOn(0, 0, 1, board);
		assertFalse(res == null);
		assertTrue(res.getNumCards_hand() == 0);
		assertTrue(res.getMinion_p0(0).getHealth() == health0);
		assertTrue(res.getMinion_p0(0).getAttack() == attack0);
		assertTrue(res.getMinion_p0(0).getMaxHealth() == health0);
		assertTrue(res.getMinion_p0(0).getTaunt());
	}

	@Test
	public void test3() {
		Minion minion0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion1 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);

		AncestralHealing fb = new AncestralHealing();
		board.placeCard_hand(fb);
		board.placeMinion_p0(minion0);
		board.placeMinion_p1(minion1);
		
		Card theCard = board.getCard_hand(0);
		BoardState res;
		
		res = theCard.useOn(0, 1, 1, board);
		assertFalse(res == null);
		assertTrue(res.getNumCards_hand() == 0);
		assertTrue(res.getMinion_p1(0).getHealth() == health0);
		assertTrue(res.getMinion_p1(0).getAttack() == attack0);
		assertTrue(res.getMinion_p1(0).getMaxHealth() == health0);
		assertTrue(res.getMinion_p1(0).getTaunt());
	}

	@Test
	public void test4() {
		Minion minion0 = new Minion("" + 0, mana, attack0, (byte)(health0-2), attack0, health0, health0);
		Minion minion1 = new Minion("" + 0, mana, attack0, (byte)(health0-2), attack0, health0, health0);

		AncestralHealing fb = new AncestralHealing();
		board.placeCard_hand(fb);
		board.placeMinion_p0(minion0);
		board.placeMinion_p1(minion1);
		
		Card theCard = board.getCard_hand(0);
		BoardState res;
		
		res = theCard.useOn(0, 1, 1, board);
		assertFalse(res == null);
		assertTrue(res.getNumCards_hand() == 0);
		assertTrue(res.getMinion_p1(0).getHealth() == health0);
		assertTrue(res.getMinion_p1(0).getAttack() == attack0);
		assertTrue(res.getMinion_p1(0).getMaxHealth() == health0);
		assertTrue(res.getMinion_p1(0).getTaunt());
	}
}
