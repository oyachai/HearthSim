package com.hearthsim.test.minion;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.concrete.ArathiWeaponsmith;
import com.hearthsim.card.weapon.concrete.FieryWarAxe;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestArathiWeaponsmith {
	private HearthTreeNode board;

	@Before
	public void setup() {
		board = new HearthTreeNode(new BoardModel());

		board.data_.getCurrentPlayer().setMana((byte)10);
		board.data_.getWaitingPlayer().setMana((byte)10);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)10);
		board.data_.getWaitingPlayer().setMaxMana((byte)10);
				
		board.data_.placeCardHandCurrentPlayer(new ArathiWeaponsmith());
	}
	
	@Test
	public void testEquipsWeapon() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
		assertEquals(board, ret);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 1);

		//should be equipped with a weapon now
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getHero().getWeaponCharge(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getHero().getTotalAttack(), 2);		
	}

	@Test
	public void testDestroysExistingWeapon() throws HSException {
		board.data_.placeCardHandCurrentPlayer(new FieryWarAxe());
		board.data_.getCurrentPlayerCardHand(1).useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 8);
		assertEquals(board.data_.getCurrentPlayerHero().getWeaponCharge(), 2);
		assertEquals(board.data_.getCurrentPlayerHero().getTotalAttack(), 3);

		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
		assertEquals(board, ret);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 1);

		//should be equipped with a weapon now
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getHero().getWeaponCharge(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getHero().getTotalAttack(), 2);		
	}
}
