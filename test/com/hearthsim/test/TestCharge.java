package com.hearthsim.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.BoardStateFactory;
import com.hearthsim.util.HearthTreeNode;
import com.hearthsim.util.StateFunction;

public class TestCharge {
	
	private BoardState board;
	private DummyStateFunc scoreFunc;
	private static final byte mana = 1;
	private static final byte attack0 = 2;
	private static final byte health0 = 3;
	private static final byte health1 = 7;

	private class DummyStateFunc implements StateFunction<BoardState> {

		@Override
		public double apply(BoardState xval) {
			return 0;
		}
	}
	
	
	@Before
	public void setup() throws HSException {
		board = new BoardState();
		scoreFunc = new DummyStateFunc();

		Minion minion1_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		board.placeMinion(1, minion1_0);
				
	}
	@Test
	public void test0() throws HSException {
		Minion minion = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1, false, false, false, true, false, false, false, false, false, false, false);
		board.placeMinion(0, minion);
		
		BoardStateFactory factory = new BoardStateFactory(null, 2000000000);
		HearthTreeNode<BoardState> tree = new HearthTreeNode<BoardState>(board);
		try {
			tree = factory.doMoves(tree, scoreFunc);			
		} catch (HSException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		
		//3 possibilites: attack enemy minion, attack enemy hero, do nothing
		assertEquals(tree.getNumNodesTried(), 3);
	}

	@Test
	public void test1() {
		Minion minion = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1, false, false, false, true, false, false, false, false, false, false, false);
		board.placeCard_hand_p0(minion);
		board.setMana_p0(1);
		
		BoardStateFactory factory = new BoardStateFactory(null);
		HearthTreeNode<BoardState> tree = new HearthTreeNode<BoardState>(board);
		try {
			tree = factory.doMoves(tree, scoreFunc);
		} catch (HSException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		
		//4 possibilities:
		// 1. Do nothing
		// 2. Play charge minion card, then don't attack
		// 3. Play card, charge attack enemy hero
		// 4. Play card, charge attack enemy minion
		assertEquals(tree.getNumNodesTried(), 4);
	}

}
