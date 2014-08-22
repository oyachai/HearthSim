package com.hearthsim.test.card;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.concrete.AncestralHealing;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestAncestralHealing {
	
	private HearthTreeNode board;
	private static final byte mana = 2;
	private static final byte attack0 = 2;
	private static final byte health0 = 5;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardState());
		Minion minion0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion1 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);

		AncestralHealing fb = new AncestralHealing();
		board.data_.placeCard_hand_p0(fb);
		board.data_.placeMinion(0, minion0);
		board.data_.placeMinion(1, minion1);
		board.data_.setMana_p0(2);
	}
	
	
	@Test
	public void test0() throws HSException {
		
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode res;
		
		try {
			Minion target = board.data_.getCharacter(0, 0);
			res = theCard.useOn(0, target, board, null, null);
			assertTrue(res == null);
		} catch (HSInvalidPlayerIndexException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		
		try {
			Minion target = board.data_.getCharacter(1, 0);
			res = theCard.useOn(1, target, board, null, null);
			assertTrue(res == null);
		} catch (HSInvalidPlayerIndexException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		
	}

	@Test
	public void test1() throws HSException {
		
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode res;
		
		try {
			Minion target = board.data_.getCharacter(0, 1);			
			res = theCard.useOn(0, target, board, null, null);
			assertFalse(res == null);
			assertTrue(res.data_.getMana_p0() == 2);
			assertTrue(res.data_.getNumCards_hand() == 0);
			assertTrue(res.data_.getMinion_p0(0).getHealth() == health0);
			assertTrue(res.data_.getMinion_p0(0).getTotalAttack() == attack0);
			assertTrue(res.data_.getMinion_p0(0).getMaxHealth() == health0);
			assertTrue(res.data_.getMinion_p0(0).getTaunt());
		} catch (HSInvalidPlayerIndexException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void test2() throws HSException {
		
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode res;
		
		try {
			Minion target = board.data_.getCharacter(0, 1);			
			res = theCard.useOn(0, target, board, null, null);
			assertFalse(res == null);
			assertTrue(res.data_.getMana_p0() == 2);
			assertTrue(res.data_.getNumCards_hand() == 0);
			assertTrue(res.data_.getMinion_p0(0).getHealth() == health0);
			assertTrue(res.data_.getMinion_p0(0).getTotalAttack() == attack0);
			assertTrue(res.data_.getMinion_p0(0).getMaxHealth() == health0);
			assertTrue(res.data_.getMinion_p0(0).getTaunt());
		} catch (HSInvalidPlayerIndexException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void test3() throws HSException {
		
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode res;
		
		try {
			Minion target = board.data_.getCharacter(1, 1);
			res = theCard.useOn(1, target, board, null, null);
			assertFalse(res == null);
			assertTrue(res.data_.getMana_p0() == 2);
			assertTrue(res.data_.getNumCards_hand() == 0);
			assertTrue(res.data_.getMinion_p1(0).getHealth() == health0);
			assertTrue(res.data_.getMinion_p1(0).getTotalAttack() == attack0);
			assertTrue(res.data_.getMinion_p1(0).getMaxHealth() == health0);
			assertTrue(res.data_.getMinion_p1(0).getTaunt());
		} catch (HSInvalidPlayerIndexException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void test4() throws HSException {
		
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode res;
		
		try {
			Minion target = board.data_.getCharacter(1, 1);
			res = theCard.useOn(1, target, board, null, null);
			assertFalse(res == null);
			assertTrue(res.data_.getMana_p0() == 2);
			assertTrue(res.data_.getNumCards_hand() == 0);
			assertTrue(res.data_.getMinion_p1(0).getHealth() == health0);
			assertTrue(res.data_.getMinion_p1(0).getTotalAttack() == attack0);
			assertTrue(res.data_.getMinion_p1(0).getMaxHealth() == health0);
			assertTrue(res.data_.getMinion_p1(0).getTaunt());
		} catch (HSInvalidPlayerIndexException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
}
