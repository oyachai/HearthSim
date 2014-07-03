package com.hearthsim.test.card;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.concrete.Fireball;
import com.hearthsim.util.BoardState;

public class TestFireball {

	private BoardState board;
	private static final byte mana = 2;
	private static final byte attack0 = 2;
	private static final byte health0 = 5;
	private static final byte health1 = 7;

	@Before
	public void setup() {
		board = new BoardState();

		Minion minion0_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion0_1 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1);
		Minion minion1_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion1_1 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1);
		
		board.placeMinion_p0(minion0_0);
		board.placeMinion_p0(minion0_1);
		
		board.placeMinion_p1(minion1_0);
		board.placeMinion_p1(minion1_1);
				
	}
	
	@Test
	public void test0() {
		Fireball fb = new Fireball();
		board.placeCard_hand(fb);
		
		Card theCard = board.getCard_hand(0);
		theCard.useOn(0, 0, 0, board);
		
		assertTrue("test0_1", board.getNumCards_hand() == 0);
		assertTrue("test0_2", board.getNumMinions_p0() == 2);
		assertTrue("test0_3", board.getNumMinions_p1() == 2);
		assertTrue("test0_4", board.getHero_p0().getHealth() == 24);
		assertTrue("test0_5", board.getHero_p1().getHealth() == 30);
		assertTrue("test0_6", board.getMinion_p0(0).getHealth() == health0);
		assertTrue("test0_7", board.getMinion_p0(1).getHealth() == health1);
		assertTrue("test0_8", board.getMinion_p1(0).getHealth() == health0);
		assertTrue("test0_9", board.getMinion_p1(1).getHealth() == health1);
	}

	@Test
	public void test1() {
		Fireball fb = new Fireball();
		board.placeCard_hand(fb);
		
		Card theCard = board.getCard_hand(0);
		theCard.useOn(0, 1, 0, board);
		
		assertTrue("test1_1", board.getNumCards_hand() == 0);
		assertTrue("test1_2", board.getHero_p0().getHealth() == 30);
		assertTrue("test1_3", board.getHero_p1().getHealth() == 24);
		assertTrue("test1_4", board.getNumMinions_p0() == 2);
		assertTrue("test1_5", board.getNumMinions_p1() == 2);
		assertTrue("test1_6", board.getMinion_p0(0).getHealth() == health0);
		assertTrue("test1_7", board.getMinion_p0(1).getHealth() == health1);
		assertTrue("test1_8", board.getMinion_p1(0).getHealth() == health0);
		assertTrue("test1_9", board.getMinion_p1(1).getHealth() == health1);
	}

	@Test
	public void test2() {
		Fireball fb = new Fireball();
		board.placeCard_hand(fb);
		
		Card theCard = board.getCard_hand(0);
		theCard.useOn(0, 0, 1, board);
		
		assertTrue("test2_1", board.getNumCards_hand() == 0);
		assertTrue("test2_2", board.getNumMinions_p0() == 1);
		assertTrue("test2_3", board.getNumMinions_p1() == 2);
		assertTrue("test2_4", board.getHero_p0().getHealth() == 30);
		assertTrue("test2_5", board.getHero_p1().getHealth() == 30);
		assertTrue("test2_6", board.getMinion_p0(0).getHealth() == health1);
		assertTrue("test2_8", board.getMinion_p1(0).getHealth() == health0);
		assertTrue("test2_9", board.getMinion_p1(1).getHealth() == health1);
	}
	
	@Test
	public void test3() {
		Fireball fb = new Fireball();
		board.placeCard_hand(fb);
		
		Card theCard = board.getCard_hand(0);
		theCard.useOn(0, 0, 2, board);
		
		assertTrue("test3_1", board.getNumCards_hand() == 0);
		assertTrue("test3_2", board.getNumMinions_p0() == 2);
		assertTrue("test3_3", board.getNumMinions_p1() == 2);
		assertTrue("test3_4", board.getHero_p0().getHealth() == 30);
		assertTrue("test3_5", board.getHero_p1().getHealth() == 30);
		assertTrue("test3_6", board.getMinion_p0(0).getHealth() == health0);
		assertTrue("test3_7", board.getMinion_p0(1).getHealth() == health1 - 6);
		assertTrue("test3_8", board.getMinion_p1(0).getHealth() == health0);
		assertTrue("test3_9", board.getMinion_p1(1).getHealth() == health1);
	}
	
	@Test
	public void test4() {
		Fireball fb = new Fireball();
		board.placeCard_hand(fb);
		
		Card theCard = board.getCard_hand(0);
		theCard.useOn(0, 1, 1, board);
		
		assertTrue("test4_1", board.getNumCards_hand() == 0);
		assertTrue("test4_2", board.getNumMinions_p0() == 2);
		assertTrue("test4_3", board.getNumMinions_p1() == 1);
		assertTrue("test4_4", board.getHero_p0().getHealth() == 30);
		assertTrue("test4_5", board.getHero_p1().getHealth() == 30);
		assertTrue("test4_6", board.getMinion_p0(0).getHealth() == health0);
		assertTrue("test4_7", board.getMinion_p0(1).getHealth() == health1);
		assertTrue("test4_8", board.getMinion_p1(0).getHealth() == health1);
	}

	@Test
	public void test5() {
		Fireball fb = new Fireball();
		board.placeCard_hand(fb);
		
		Card theCard = board.getCard_hand(0);
		theCard.useOn(0, 1, 2, board);
		
		assertTrue("test5_1", board.getNumCards_hand() == 0);
		assertTrue("test5_2", board.getNumMinions_p0() == 2);
		assertTrue("test5_3", board.getNumMinions_p1() == 2);
		assertTrue("test5_4", board.getHero_p0().getHealth() == 30);
		assertTrue("test5_5", board.getHero_p1().getHealth() == 30);
		assertTrue("test5_6", board.getMinion_p0(0).getHealth() == health0);
		assertTrue("test5_7", board.getMinion_p0(1).getHealth() == health1);
		assertTrue("test5_8", board.getMinion_p1(0).getHealth() == health0);
		assertTrue("test5_9", board.getMinion_p1(1).getHealth() == health1 - 6);
	}
}
