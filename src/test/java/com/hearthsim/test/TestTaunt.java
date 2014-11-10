package com.hearthsim.test;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.concrete.HolySmite;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestTaunt {

	private BoardModel board;
	private DummyStateFunc scoreFunc;
	private static final byte mana = 1;
	private static final byte attack0 = 2;
	private static final byte health0 = 3;
	private static final byte health1 = 7;

	private class DummyStateFunc extends BruteForceSearchAI {

		@Override
		public double boardScore(BoardModel xval) {
			return 0;
		}

	}

	@Before
	public void setup() throws HSException {
		board = new BoardModel();
		scoreFunc = new DummyStateFunc();

		Minion minion0_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);

		board.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);

	}

	@Test
	public void test0() throws HSException {
		Minion minion = new Minion("" + 0, mana, attack0, health1, attack0, (byte) 0, (byte) 0, health1, health1,
				(byte) 0, (byte) 0, true, false, false, false, false, false, false, false, false, true, false, false,
				false, false, null, null, false, false);
		board.placeMinion(PlayerSide.WAITING_PLAYER, minion);

		BoardStateFactoryBase factory = new BoardStateFactoryBase(null, null);
		HearthTreeNode tree = new HearthTreeNode(board);
		try {
			tree = factory.doMoves(tree, scoreFunc);
		} catch (HSException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		// 2 possibilities:
		// 1. Do nothing
		// 2. Attack the Taunt minion
		assertEquals(tree.getNumNodesTried(), 2);
	}

	@Test
	public void test1() throws HSException {
		Minion minion1 = new Minion("" + 0, mana, attack0, health1, attack0, (byte) 0, (byte) 0, health1, health1,
				(byte) 0, (byte) 0, true, false, false, false, false, false, false, false, false, true, false, false,
				false, false, null, null, false, false);
		Minion minion2 = new Minion("" + 0, mana, attack0, health1, attack0, (byte) 0, (byte) 0, health1, health1,
				(byte) 0, (byte) 0, false, false, false, false, false, false, false, false, false, true, false, false,
				false, false, null, null, false, false);
		board.placeMinion(PlayerSide.WAITING_PLAYER, minion1);
		board.placeMinion(PlayerSide.WAITING_PLAYER, minion2);

		BoardStateFactoryBase factory = new BoardStateFactoryBase(null, null);
		HearthTreeNode tree = new HearthTreeNode(board);
		try {
			tree = factory.doMoves(tree, scoreFunc);
		} catch (HSException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		// 2 possibilities:
		// 1. Do nothing
		// 2. Attack the Taunt minion
		assertEquals(tree.getNumNodesTried(), 2);
	}

	@Test
	public void test2() throws HSException {
		Minion minion1 = new Minion("" + 0, mana, attack0, health1, attack0, (byte) 0, (byte) 0, health1, health1,
				(byte) 0, (byte) 0, true, false, false, false, false, false, false, false, false, true, false, false,
				false, false, null, null, false, false);
		Minion minion2 = new Minion("" + 0, mana, attack0, health1, attack0, (byte) 0, (byte) 0, health1, health1,
				(byte) 0, (byte) 0, false, false, false, false, false, false, false, false, false, true, false, false,
				false, false, null, null, false, false);
		Minion minion3 = new Minion("" + 0, mana, attack0, health1, attack0, (byte) 0, (byte) 0, health1, health1,
				(byte) 0, (byte) 0, true, false, false, false, false, false, false, false, false, true, false, false,
				false, false, null, null, false, false);
		board.placeMinion(PlayerSide.WAITING_PLAYER, minion1);
		board.placeMinion(PlayerSide.WAITING_PLAYER, minion2);
		board.placeMinion(PlayerSide.WAITING_PLAYER, minion3);

		BoardStateFactoryBase factory = new BoardStateFactoryBase(null, null);
		HearthTreeNode tree = new HearthTreeNode(board);
		try {
			tree = factory.doMoves(tree, scoreFunc);
		} catch (HSException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		// 3 possibilities:
		// 1. Do nothing
		// 2. Attack the Taunt minion1
		// 3. Attack the Taunt minion2
		assertEquals(tree.getNumNodesTried(), 3);
	}

	@Test
	public void test3() throws HSException {
		HolySmite hs = new HolySmite();
		Minion minion1 = new Minion("" + 0, mana, attack0, health1, attack0, (byte) 0, (byte) 0, health1, health1,
				(byte) 0, (byte) 0, true, false, false, false, false, false, false, false, false, true, false, false,
				false, false, null, null, false, false);

		board.removeMinion(PlayerSide.CURRENT_PLAYER, 0);
		board.placeCardHandCurrentPlayer(hs);
		board.placeMinion(PlayerSide.WAITING_PLAYER, minion1);

		BoardStateFactoryBase factory = new BoardStateFactoryBase(null, null, 2000000000);
		HearthTreeNode tree = new HearthTreeNode(board);
		try {
			tree = factory.doMoves(tree, scoreFunc);
		} catch (HSException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		// 1 possibility:
		// 1. Do nothing (not enough mana!)
		assertEquals(tree.getNumNodesTried(), 1);

	}

	@Test
	public void test4() throws HSException {
		HolySmite hs = new HolySmite();
		Minion minion1 = new Minion("" + 0, mana, attack0, health1, attack0, (byte) 0, (byte) 0, health1, health1,
				(byte) 0, (byte) 0, true, false, false, false, false, false, false, false, false, true, false, false,
				false, false, null, null, false, false);

		board.removeMinion(PlayerSide.CURRENT_PLAYER, 0);
		board.placeCardHandCurrentPlayer(hs);
		board.placeMinion(PlayerSide.WAITING_PLAYER, minion1);
		board.getCurrentPlayer().setMana(1);

		BoardStateFactoryBase factory = new BoardStateFactoryBase(null, null);
		HearthTreeNode tree = new HearthTreeNode(board);
		try {
			tree = factory.doMoves(tree, scoreFunc);
		} catch (HSException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		// 4 possibilities:
		// 1. Do nothing
		// 2. Use HS on the Taunt minion1
		// 3. Use HS on the enemy hero
		// 4. Use HS on the my own hero
		assertEquals(tree.getNumNodesTried(), 4);

	}

	@Test
	public void test5() throws HSException {
		HolySmite hs = new HolySmite();
		Minion minion1 = new Minion("" + 0, mana, attack0, health1, attack0, (byte) 0, (byte) 0, health1, health1,
				(byte) 0, (byte) 0, true, false, false, false, false, false, false, false, false, true, false, false,
				false, false, null, null, false, false);

		board.removeMinion(PlayerSide.CURRENT_PLAYER, 0);
		board.placeCardHandCurrentPlayer(hs);
		board.placeMinion(PlayerSide.WAITING_PLAYER, minion1);
		board.getCurrentPlayer().setMana(2);

		BoardStateFactoryBase factory = new BoardStateFactoryBase(null, null, 2000000000);
		HearthTreeNode tree = new HearthTreeNode(board);
		try {
			tree = factory.doMoves(tree, scoreFunc);
		} catch (HSException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		// 4 possibilities:
		// 1. Do nothing
		// 2. Use HS on the Taunt minion1
		// 3. Use HS on the enemy hero
		// 4. Use HS on the my own hero
		assertEquals(tree.getNumNodesTried(), 4);

	}
}
