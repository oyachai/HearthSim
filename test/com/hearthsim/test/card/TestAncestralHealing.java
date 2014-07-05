package com.hearthsim.test.card;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.concrete.AncestralHealing;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.HearthTreeNode;

public class TestAncestralHealing {
	
	private HearthTreeNode<BoardState> board;
	private static final byte mana = 2;
	private static final byte attack0 = 2;
	private static final byte health0 = 5;

	@Before
	public void setup() {
		board = new HearthTreeNode<BoardState>(new BoardState());
		Minion minion0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion1 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);

		AncestralHealing fb = new AncestralHealing();
		board.data_.placeCard_hand(fb);
		board.data_.placeMinion_p0(minion0);
		board.data_.placeMinion_p1(minion1);
		board.data_.setMana_p0(2);
	}
	
	
	@Test
	public void test0() {
		
		Card theCard = board.data_.getCard_hand(0);
		HearthTreeNode<BoardState> res;
		
		res = theCard.useOn(0, 0, 0, board, null);
		assertTrue(res == null);
		
		res = theCard.useOn(0, 1, 0, board, null);
		assertTrue(res == null);
		
	}

	@Test
	public void test1() {
		
		Card theCard = board.data_.getCard_hand(0);
		HearthTreeNode<BoardState> res;
		
		res = theCard.useOn(0, 0, 1, board, null);
		assertFalse(res == null);
		assertTrue(res.data_.getMana_p0() == 2);
		assertTrue(res.data_.getNumCards_hand() == 0);
		assertTrue(res.data_.getMinion_p0(0).getHealth() == health0);
		assertTrue(res.data_.getMinion_p0(0).getAttack() == attack0);
		assertTrue(res.data_.getMinion_p0(0).getMaxHealth() == health0);
		assertTrue(res.data_.getMinion_p0(0).getTaunt());
	}

	@Test
	public void test2() {
		
		Card theCard = board.data_.getCard_hand(0);
		HearthTreeNode<BoardState> res;
		
		res = theCard.useOn(0, 0, 1, board, null);
		assertFalse(res == null);
		assertTrue(res.data_.getMana_p0() == 2);
		assertTrue(res.data_.getNumCards_hand() == 0);
		assertTrue(res.data_.getMinion_p0(0).getHealth() == health0);
		assertTrue(res.data_.getMinion_p0(0).getAttack() == attack0);
		assertTrue(res.data_.getMinion_p0(0).getMaxHealth() == health0);
		assertTrue(res.data_.getMinion_p0(0).getTaunt());
	}

	@Test
	public void test3() {
		
		Card theCard = board.data_.getCard_hand(0);
		HearthTreeNode<BoardState> res;
		
		res = theCard.useOn(0, 1, 1, board, null);
		assertFalse(res == null);
		assertTrue(res.data_.getMana_p0() == 2);
		assertTrue(res.data_.getNumCards_hand() == 0);
		assertTrue(res.data_.getMinion_p1(0).getHealth() == health0);
		assertTrue(res.data_.getMinion_p1(0).getAttack() == attack0);
		assertTrue(res.data_.getMinion_p1(0).getMaxHealth() == health0);
		assertTrue(res.data_.getMinion_p1(0).getTaunt());
	}

	@Test
	public void test4() {
		
		Card theCard = board.data_.getCard_hand(0);
		HearthTreeNode<BoardState> res;
		
		res = theCard.useOn(0, 1, 1, board, null);
		assertFalse(res == null);
		assertTrue(res.data_.getMana_p0() == 2);
		assertTrue(res.data_.getNumCards_hand() == 0);
		assertTrue(res.data_.getMinion_p1(0).getHealth() == health0);
		assertTrue(res.data_.getMinion_p1(0).getAttack() == attack0);
		assertTrue(res.data_.getMinion_p1(0).getMaxHealth() == health0);
		assertTrue(res.data_.getMinion_p1(0).getTaunt());
	}
}
