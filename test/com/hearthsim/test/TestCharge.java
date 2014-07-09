package com.hearthsim.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.BoardStateFactory;
import com.hearthsim.util.HearthTreeNode;

public class TestCharge {
	
	private BoardState board;
	private static final byte mana = 2;
	private static final byte attack0 = 2;
	private static final byte health0 = 3;
	private static final byte health1 = 7;

	@Before
	public void setup() {
		board = new BoardState();

		Minion minion1_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		board.placeMinion_p1(minion1_0);
				
	}
	@Test
	public void test0() {
		Minion minion = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1, false, false, false, true, false, false, false, false, false, false, false);
		board.placeMinion_p0(minion);
		
		BoardStateFactory factory = new BoardStateFactory(null);
		HearthTreeNode<BoardState> tree = new HearthTreeNode<BoardState>(board);
		try {
			tree = factory.doMoves(tree);			
		} catch (HSInvalidPlayerIndexException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		
		assertTrue(tree.numChildren() == 3);
		for (HearthTreeNode<BoardState> child : tree.getChildren()) {
			assertTrue(child.numChildren() == 0);
		}
	}

	@Test
	public void test1() {
		Minion minion = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1, false, false, false, true, false, false, false, false, false, false, false);
		board.placeCard_hand_p0(minion);
		board.setMana_p0(2);
		
		BoardStateFactory factory = new BoardStateFactory(null);
		HearthTreeNode<BoardState> tree = new HearthTreeNode<BoardState>(board);
		try {
			tree = factory.doMoves(tree);
		} catch (HSInvalidPlayerIndexException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		
		assertTrue(tree.numChildren() == 2);
		for (HearthTreeNode<BoardState> child : tree.getChildren()) {
			if (child.numChildren() > 0) {
				assertTrue(child.numChildren() == 3);
			}
		}
	}

}
