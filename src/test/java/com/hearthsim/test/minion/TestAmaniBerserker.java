package com.hearthsim.test.minion;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.AmaniBerserker;
import com.hearthsim.card.minion.concrete.RiverCrocolisk;
import com.hearthsim.card.spellcard.concrete.HolyLight;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

/**
 * Serves as a general test for enrage mechanic
 */
public class TestAmaniBerserker {

	private HearthTreeNode board;
	private AmaniBerserker amaniBerserker;
	private RiverCrocolisk croc;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel());

		amaniBerserker = new AmaniBerserker();
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, amaniBerserker);

		croc = new RiverCrocolisk();
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, croc);
	}

	@Test
	public void testAttackNormal() throws HSException {
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		HearthTreeNode ret = amaniBerserker.attack(PlayerSide.WAITING_PLAYER, target, board, null, null);
		assertEquals(board, ret);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 28);
	}

	@Test
	public void testEnrage() throws HSException {
		HearthTreeNode ret = amaniBerserker.attack(PlayerSide.WAITING_PLAYER, croc, board, null, null);
		assertEquals(board, ret);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 1);

		assertEquals(1, amaniBerserker.getHealth());
		assertEquals(1, croc.getHealth());

		assertEquals(2 + 3, amaniBerserker.getAttack()); // enraged!
	}

	@Test
	public void testHealRemovesEngrage() throws HSException {
		HearthTreeNode ret = amaniBerserker.attack(PlayerSide.WAITING_PLAYER, croc, board, null, null);

		board.data_.placeCardHandCurrentPlayer(new HolyLight());
		board.data_.getCurrentPlayer().setMana((byte)2);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, amaniBerserker, board, null, null);

		assertEquals(board, ret);
		assertEquals(3, amaniBerserker.getHealth());
		assertEquals(2, amaniBerserker.getAttack()); // not enraged anymore!
	}
}
