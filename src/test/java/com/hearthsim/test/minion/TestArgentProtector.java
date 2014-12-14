package com.hearthsim.test.minion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.hearthsim.card.minion.concrete.ArgentProtector;
import com.hearthsim.card.minion.concrete.ArgentSquire;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestArgentProtector {
	private HearthTreeNode board;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel());
	}

	@Test
	public void testAddsDivineShield() throws HSException {
		BoulderfistOgre ogre = new BoulderfistOgre();
		ArgentProtector protector = new ArgentProtector();
		protector.useTargetableBattlecry_core(PlayerSide.WAITING_PLAYER, ogre, board, null, null);
		assertTrue(ogre.getDivineShield());
	}

	@Test
	@Ignore("Unverified behavior")
	// TODO verify behavior
	public void testCannotTargetDivineShield() throws HSException {
		ArgentProtector protector = new ArgentProtector();
		board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, protector);

		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BoulderfistOgre());
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new ArgentSquire());

		HearthTreeNode ret = protector.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
		assertEquals(board, ret);

		assertEquals(board.numChildren(), 1);
	}
}
