package com.hearthsim.test;

import com.hearthsim.card.minion.Minion;
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

public class TestCharge {

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

		Minion minion1_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		board.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);

	}

	@Test
	public void test0() throws HSException {
		Minion minion = new Minion("" + 0, mana, attack0, health1, attack0, (byte) 0, (byte) 0, health1, health1,
				(byte) 0, (byte) 0, false, false, false, true, false, false, false, false, false, false, false, false,
				false, false, null, null, false, false);
		board.placeMinion(PlayerSide.CURRENT_PLAYER, minion);

		BoardStateFactoryBase factory = new BoardStateFactoryBase(null, null, 2000000000);
		HearthTreeNode tree = new HearthTreeNode(board);
		try {
			tree = factory.doMoves(tree, scoreFunc);
		} catch (HSException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		// 3 possibilites: attack enemy minion, attack enemy hero, do nothing
		assertEquals(tree.getNumNodesTried(), 3);
	}

	@Test
	public void test1() {
		Minion minion = new Minion("" + 0, mana, attack0, health1, attack0, (byte) 0, (byte) 0, health1, health1,
				(byte) 0, (byte) 0, false, false, false, true, false, false, false, false, false, false, false, false,
				false, false, null, null, true, false);
		board.placeCardHandCurrentPlayer(minion);
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
		// 2. Play charge minion card, then don't attack
		// 3. Play card, charge attack enemy hero
		// 4. Play card, charge attack enemy minion
		assertEquals(tree.getNumNodesTried(), 4);
	}

}
