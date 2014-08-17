package com.hearthsim.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.BoardStateFactory;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.card.spellcard.concrete.*;
import com.hearthsim.exception.HSException;

public class TestTaunt {
	
	private BoardState board;
	private DummyStateFunc scoreFunc;
	private static final byte mana = 1;
	private static final byte attack0 = 2;
	private static final byte health0 = 3;
	private static final byte health1 = 7;

	private class DummyStateFunc extends ArtificialPlayer {

		@Override
		public double boardScore(BoardState xval) {
			return 0;
		}
		
	}
	
	@Before
	public void setup() throws HSException {
		board = new BoardState();
		scoreFunc = new DummyStateFunc();

		Minion minion0_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		
		board.placeMinion(0, minion0_0);
				
	}
	
	@Test
	public void test0() throws HSException {
		Minion minion = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1, true, false, false, false, false, false, false, false, false, false, false, false);
		board.placeMinion(1, minion);
		
		BoardStateFactory factory = new BoardStateFactory(null, null);
		HearthTreeNode tree = new HearthTreeNode(board);
		try {
			tree = factory.doMoves(tree, scoreFunc);
		} catch (HSException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		
		//2 possibilities:
		// 1. Do nothing
		// 2. Attack the Taunt minion
		assertEquals(tree.getNumNodesTried(), 2);
	}

	@Test
	public void test1() throws HSException {
		Minion minion1 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1, true, false, false, false, false, false, false, false, false, false, false, false);
		Minion minion2 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1, false, false, false, false, false, false, false, false, false, false, false, false);
		board.placeMinion(1, minion1);
		board.placeMinion(1, minion2);
		
		BoardStateFactory factory = new BoardStateFactory(null, null);
		HearthTreeNode tree = new HearthTreeNode(board);
		try {
			tree = factory.doMoves(tree, scoreFunc);
		} catch (HSException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		
		//2 possibilities:
		// 1. Do nothing
		// 2. Attack the Taunt minion
		assertEquals(tree.getNumNodesTried(), 2);
	}

	@Test
	public void test2() throws HSException {
		Minion minion1 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1, true, false, false, false, false, false, false, false, false, false, false, false);
		Minion minion2 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1, false, false, false, false, false, false, false, false, false, false, false, false);
		Minion minion3 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1, true, false, false, false, false, false, false, false, false, false, false, false);
		board.placeMinion(1, minion1);
		board.placeMinion(1, minion2);
		board.placeMinion(1, minion3);
		
		BoardStateFactory factory = new BoardStateFactory(null, null);
		HearthTreeNode tree = new HearthTreeNode(board);
		try {
			tree = factory.doMoves(tree, scoreFunc);
		} catch (HSException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		
		//3 possibilities:
		// 1. Do nothing
		// 2. Attack the Taunt minion1
		// 3. Attack the Taunt minion2
		assertEquals(tree.getNumNodesTried(), 3);
	}
	
	@Test
	public void test3() throws HSException {
		HolySmite hs = new HolySmite();
		Minion minion1 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1, true, false, false, false, false, false, false, false, false, false, false, false);

		board.removeMinion(0, 0);
		board.placeCard_hand_p0(hs);
		board.placeMinion(1, minion1);
		
		BoardStateFactory factory = new BoardStateFactory(null, null, 2000000000);
		HearthTreeNode tree = new HearthTreeNode(board);
		try {
			tree = factory.doMoves(tree, scoreFunc);
		} catch (HSException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		
		//1 possibility:
		// 1. Do nothing (not enough mana!)
		assertEquals(tree.getNumNodesTried(), 1);

	}

	@Test
	public void test4() throws HSException {
		HolySmite hs = new HolySmite();
		Minion minion1 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1, true, false, false, false, false, false, false, false, false, false, false, false);

		board.removeMinion(0, 0);
		board.placeCard_hand_p0(hs);
		board.placeMinion(1, minion1);
		board.setMana_p0(1);
		
		BoardStateFactory factory = new BoardStateFactory(null, null);
		HearthTreeNode tree = new HearthTreeNode(board);
		try {
			tree = factory.doMoves(tree, scoreFunc);
		} catch (HSException e) {
			e.printStackTrace();
			assertTrue(false);
		}				
		//4 possibilities:
		// 1. Do nothing
		// 2. Use HS on the Taunt minion1
		// 3. Use HS on the enemy hero
		// 4. Use HS on the my own hero
		assertEquals(tree.getNumNodesTried(), 4);

	}

	@Test
	public void test5() throws HSException {
		HolySmite hs = new HolySmite();
		Minion minion1 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1, true, false, false, false, false, false, false, false, false, false, false, false);

		board.removeMinion(0, 0);
		board.placeCard_hand_p0(hs);
		board.placeMinion(1, minion1);
		board.setMana_p0(2);
		
		BoardStateFactory factory = new BoardStateFactory(null, null, 2000000000);
		HearthTreeNode tree = new HearthTreeNode(board);
		try {
			tree = factory.doMoves(tree, scoreFunc);
		} catch (HSException e) {
			e.printStackTrace();
			assertTrue(false);
		}				
		//4 possibilities:
		// 1. Do nothing
		// 2. Use HS on the Taunt minion1
		// 3. Use HS on the enemy hero
		// 4. Use HS on the my own hero
		assertEquals(tree.getNumNodesTried(), 4);

	}
}
