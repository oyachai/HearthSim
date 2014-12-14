package com.hearthsim.test.minion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.GurubashiBerserker;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestGurubashiBerserker {

	private HearthTreeNode board;
	private static final byte mana = 2;
	private static final byte attack0 = 5;
	private static final byte health0 = 3;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel());

		Minion minion1_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);		
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);

		Minion fb = new GurubashiBerserker();
		board.data_.placeCardHandCurrentPlayer(fb);

		board.data_.getCurrentPlayer().setMana((byte)7);
	}
	
	@Test
	public void testBuffsAfterAttackingEnemyMinion() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
		assertEquals(board, ret);

		Minion berserker = board.data_.getCurrentPlayer().getMinions().get(0);
		berserker.hasAttacked(false);
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 1);
		ret = berserker.attack(PlayerSide.WAITING_PLAYER, target, board, null, null);
		assertEquals(board, ret);

		assertEquals(berserker.getHealth(), 7 - attack0);
		assertEquals(berserker.getTotalAttack(), 5);
	}

	@Test
	public void testDivineShieldPreventsBuff() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
		assertEquals(board, ret);

		Minion berserker = board.data_.getCurrentPlayer().getMinions().get(0);
		berserker.hasAttacked(false);
		berserker.setDivineShield(true);
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 1);
		ret = berserker.attack(PlayerSide.WAITING_PLAYER, target, board, null, null);
		assertEquals(board, ret);

		assertEquals(berserker.getHealth(), 7);
		assertEquals(berserker.getTotalAttack(), 2);
		assertFalse(berserker.getDivineShield());
	}
}
