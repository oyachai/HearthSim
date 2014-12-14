package com.hearthsim.test.minion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.DustDevil;
import com.hearthsim.card.minion.concrete.Windspeaker;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestWindspeaker {
	private HearthTreeNode board;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel());
	}

	@Test
	public void testAddsWindfury() throws HSException {
		BoulderfistOgre ogre = new BoulderfistOgre();
		Windspeaker windspeaker = new Windspeaker();
		windspeaker.useTargetableBattlecry_core(PlayerSide.WAITING_PLAYER, ogre, board, null, null);
		assertTrue(ogre.getWindfury());
	}

	@Test
	@Ignore("Unverified behavior")
	// TODO verify behavior
	public void testCannotTargetWindfury() throws HSException {
		Windspeaker windspeaker = new Windspeaker();
		board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, windspeaker);

		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BoulderfistOgre());
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new DustDevil());

		HearthTreeNode ret = windspeaker.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
		assertEquals(board, ret);

		assertEquals(board.numChildren(), 1);
	}}
