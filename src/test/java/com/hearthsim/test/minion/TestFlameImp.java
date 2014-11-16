package com.hearthsim.test.minion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.FlameImp;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestFlameImp {

	private HearthTreeNode board;

	@Before
	public void setup() {
		board = new HearthTreeNode(new BoardModel());
		board.data_.placeCardHandCurrentPlayer(new FlameImp());

		board.data_.getCurrentPlayer().setMana((byte)8);
		board.data_.getCurrentPlayer().setMaxMana((byte)8);
	}

	@Test
	public void testDamagesOwnHero() throws HSException {
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, target, board, null, null);

		assertEquals(board, ret);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 7);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 27);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
	}

	@Test
	public void testKillsOwnHero() throws HSException {
		board.data_.getCurrentPlayerHero().setHealth((byte)2);

		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, target, board, null, null);
		assertEquals(board, ret);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 7);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), -1);
		
		assertTrue(board.data_.isLethalState());
	}
}
