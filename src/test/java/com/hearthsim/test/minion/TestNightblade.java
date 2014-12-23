package com.hearthsim.test.minion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.concrete.Nightblade;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestNightblade {

	private HearthTreeNode board;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel());

		Card fb = new Nightblade();
		board.data_.placeCardHandCurrentPlayer(fb);

		board.data_.getCurrentPlayer().setMana((byte)10);
		board.data_.getCurrentPlayer().setMaxMana((byte)7);

	}

	@Test
	public void testDamagesEnemyHero() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
		assertEquals(board, ret);

		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 5);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 27);
	}

	@Test
	public void testKillsEnemyHero() throws HSException {
		board.data_.getWaitingPlayerHero().setHealth((byte)2);

		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
		assertEquals(board, ret);

		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 5);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), -1);
		
		assertTrue(board.data_.isLethalState());
	}
}
