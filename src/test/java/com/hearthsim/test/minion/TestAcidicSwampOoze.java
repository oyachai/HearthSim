package com.hearthsim.test.minion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.concrete.AcidicSwampOoze;
import com.hearthsim.card.weapon.concrete.FieryWarAxe;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestAcidicSwampOoze {

	private HearthTreeNode board;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel());

		AcidicSwampOoze ooze = new AcidicSwampOoze();
		board.data_.placeCardHandCurrentPlayer(ooze);

		board.data_.getCurrentPlayer().setMana((byte)4);
		board.data_.getWaitingPlayer().setMana((byte)4);

		board.data_.getCurrentPlayer().setMaxMana((byte)4);
		board.data_.getWaitingPlayer().setMaxMana((byte)4);
	}

	@Test
	public void testDestroysWeapon() throws HSException {
		FieryWarAxe axe = new FieryWarAxe();
		board.data_.getWaitingPlayerHero().setWeapon(axe);

		assertEquals(board.data_.getCurrentPlayerHero().getTotalAttack(), 0);
		assertEquals(board.data_.getCurrentPlayerHero().getWeaponCharge(), 0);
		assertEquals(board.data_.getWaitingPlayerHero().getWeapon(), axe);
		assertEquals(board.data_.getWaitingPlayerHero().getTotalAttack(), 3);
		assertEquals(board.data_.getWaitingPlayerHero().getWeaponCharge(), 2);

		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);

		assertEquals(board, ret);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 1);

		assertEquals(board.data_.getCurrentPlayerHero().getTotalAttack(), 0);
		assertEquals(board.data_.getCurrentPlayerHero().getWeaponCharge(), 0);
		assertNull(board.data_.getWaitingPlayerHero().getWeapon());
		assertEquals(board.data_.getWaitingPlayerHero().getTotalAttack(), 0);
		assertEquals(board.data_.getWaitingPlayerHero().getWeaponCharge(), 0);
	}
}
