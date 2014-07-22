package com.hearthsim.test.card;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.concrete.Fireball;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestFireball {

	private HearthTreeNode board;
	private static final byte mana = 2;
	private static final byte attack0 = 2;
	private static final byte health0 = 5;
	private static final byte health1 = 7;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode( new BoardState() );

		Minion minion0_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion0_1 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1);
		Minion minion1_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion1_1 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1);
		
		board.data_.placeMinion(0, minion0_0);
		board.data_.placeMinion(0, minion0_1);
		
		board.data_.placeMinion(1, minion1_0);
		board.data_.placeMinion(1, minion1_1);
				
	}
	
	@Test
	public void test0() throws HSInvalidPlayerIndexException {
		Fireball fb = new Fireball();
		board.data_.placeCard_hand_p0(fb);
		
		Card theCard = board.data_.getCard_hand_p0(0);
		theCard.useOn(0, 0, 0, board, null, null);
		
		assertTrue("test0_1", board.data_.getNumCards_hand() == 0);
		assertTrue("test0_2", board.data_.getNumMinions_p0() == 2);
		assertTrue("test0_3", board.data_.getNumMinions_p1() == 2);
		assertTrue("test0_4", board.data_.getHero_p0().getHealth() == 24);
		assertTrue("test0_5", board.data_.getHero_p1().getHealth() == 30);
		assertTrue("test0_6", board.data_.getMinion_p0(0).getHealth() == health0);
		assertTrue("test0_7", board.data_.getMinion_p0(1).getHealth() == health1);
		assertTrue("test0_8", board.data_.getMinion_p1(0).getHealth() == health0);
		assertTrue("test0_9", board.data_.getMinion_p1(1).getHealth() == health1);
	}

	@Test
	public void test1() throws HSInvalidPlayerIndexException {
		Fireball fb = new Fireball();
		board.data_.placeCard_hand_p0(fb);
		
		Card theCard = board.data_.getCard_hand_p0(0);
		theCard.useOn(0, 1, 0, board, null, null);
		
		assertTrue("test1_1", board.data_.getNumCards_hand() == 0);
		assertTrue("test1_2", board.data_.getHero_p0().getHealth() == 30);
		assertTrue("test1_3", board.data_.getHero_p1().getHealth() == 24);
		assertTrue("test1_4", board.data_.getNumMinions_p0() == 2);
		assertTrue("test1_5", board.data_.getNumMinions_p1() == 2);
		assertTrue("test1_6", board.data_.getMinion_p0(0).getHealth() == health0);
		assertTrue("test1_7", board.data_.getMinion_p0(1).getHealth() == health1);
		assertTrue("test1_8", board.data_.getMinion_p1(0).getHealth() == health0);
		assertTrue("test1_9", board.data_.getMinion_p1(1).getHealth() == health1);
	}

	@Test
	public void test2() throws HSInvalidPlayerIndexException {
		Fireball fb = new Fireball();
		board.data_.placeCard_hand_p0(fb);
		
		Card theCard = board.data_.getCard_hand_p0(0);
		theCard.useOn(0, 0, 1, board, null, null);
		
		assertTrue("test2_1", board.data_.getNumCards_hand() == 0);
		assertTrue("test2_2", board.data_.getNumMinions_p0() == 1);
		assertTrue("test2_3", board.data_.getNumMinions_p1() == 2);
		assertTrue("test2_4", board.data_.getHero_p0().getHealth() == 30);
		assertTrue("test2_5", board.data_.getHero_p1().getHealth() == 30);
		assertTrue("test2_6", board.data_.getMinion_p0(0).getHealth() == health1);
		assertTrue("test2_8", board.data_.getMinion_p1(0).getHealth() == health0);
		assertTrue("test2_9", board.data_.getMinion_p1(1).getHealth() == health1);
	}
	
	@Test
	public void test3() throws HSInvalidPlayerIndexException {
		Fireball fb = new Fireball();
		board.data_.placeCard_hand_p0(fb);
		
		Card theCard = board.data_.getCard_hand_p0(0);
		theCard.useOn(0, 0, 2, board, null, null);
		
		assertTrue("test3_1", board.data_.getNumCards_hand() == 0);
		assertTrue("test3_2", board.data_.getNumMinions_p0() == 2);
		assertTrue("test3_3", board.data_.getNumMinions_p1() == 2);
		assertTrue("test3_4", board.data_.getHero_p0().getHealth() == 30);
		assertTrue("test3_5", board.data_.getHero_p1().getHealth() == 30);
		assertTrue("test3_6", board.data_.getMinion_p0(0).getHealth() == health0);
		assertTrue("test3_7", board.data_.getMinion_p0(1).getHealth() == health1 - 6);
		assertTrue("test3_8", board.data_.getMinion_p1(0).getHealth() == health0);
		assertTrue("test3_9", board.data_.getMinion_p1(1).getHealth() == health1);
	}
	
	@Test
	public void test4() throws HSInvalidPlayerIndexException {
		Fireball fb = new Fireball();
		board.data_.placeCard_hand_p0(fb);
		
		Card theCard = board.data_.getCard_hand_p0(0);
		theCard.useOn(0, 1, 1, board, null, null);
		
		assertTrue("test4_1", board.data_.getNumCards_hand() == 0);
		assertTrue("test4_2", board.data_.getNumMinions_p0() == 2);
		assertTrue("test4_3", board.data_.getNumMinions_p1() == 1);
		assertTrue("test4_4", board.data_.getHero_p0().getHealth() == 30);
		assertTrue("test4_5", board.data_.getHero_p1().getHealth() == 30);
		assertTrue("test4_6", board.data_.getMinion_p0(0).getHealth() == health0);
		assertTrue("test4_7", board.data_.getMinion_p0(1).getHealth() == health1);
		assertTrue("test4_8", board.data_.getMinion_p1(0).getHealth() == health1);
	}

	@Test
	public void test5() throws HSInvalidPlayerIndexException {
		Fireball fb = new Fireball();
		board.data_.placeCard_hand_p0(fb);
		
		Card theCard = board.data_.getCard_hand_p0(0);
		theCard.useOn(0, 1, 2, board, null, null);
		
		assertTrue("test5_1", board.data_.getNumCards_hand() == 0);
		assertTrue("test5_2", board.data_.getNumMinions_p0() == 2);
		assertTrue("test5_3", board.data_.getNumMinions_p1() == 2);
		assertTrue("test5_4", board.data_.getHero_p0().getHealth() == 30);
		assertTrue("test5_5", board.data_.getHero_p1().getHealth() == 30);
		assertTrue("test5_6", board.data_.getMinion_p0(0).getHealth() == health0);
		assertTrue("test5_7", board.data_.getMinion_p0(1).getHealth() == health1);
		assertTrue("test5_8", board.data_.getMinion_p1(0).getHealth() == health0);
		assertTrue("test5_9", board.data_.getMinion_p1(1).getHealth() == health1 - 6);
	}
}
