package com.hearthsim.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.KoboldGeomancer;
import com.hearthsim.card.spellcard.concrete.ArcaneExplosion;
import com.hearthsim.card.spellcard.concrete.Consecration;
import com.hearthsim.card.spellcard.concrete.Hellfire;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestSpellDamageAoe {

	private HearthTreeNode board;
	private static final byte mana = 2;
	private static final byte attack0 = 2;
	private static final byte health0 = 5;
	private static final byte health1 = 1;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel());

		Minion minion0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion1 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion2 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1);
		Minion minion3 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);

		ArcaneExplosion fb = new ArcaneExplosion();
		board.data_.placeCardHandCurrentPlayer(fb);
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion2);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion3);

		board.data_.getCurrentPlayer().setMana((byte)3);
	}

	@Test
	public void testHitsEnemyMinions() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 0, board, null, null);
		assertEquals(board, ret);

		assertEquals(board.data_.getCurrentPlayer().getMana(), 1);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);

		assertEquals(board.data_.getCurrentPlayer().getNumMinions(), 1);
		assertEquals(board.data_.getWaitingPlayer().getNumMinions(), 2);

		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getHealth(), health0);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), attack0);

		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getHealth(), health0 - 1);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), attack0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getHealth(), health0 - 1);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), attack0);
	}

	@Test
	public void testHitsEnemyCharacters() throws HSException {
		Consecration consecration = new Consecration();
		board.data_.placeCardHandCurrentPlayer(consecration);
		board.data_.getCurrentPlayer().setMana((byte)5);

		Card theCard = board.data_.getCurrentPlayerCardHand(1);
		HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 0, board, null, null);
		assertEquals(board, ret);

		assertEquals(board.data_.getCurrentPlayer().getMana(), 1);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 28);

		assertEquals(board.data_.getCurrentPlayer().getNumMinions(), 1);
		assertEquals(board.data_.getWaitingPlayer().getNumMinions(), 2);

		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getHealth(), health0);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), attack0);

		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getHealth(), health0 - 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), attack0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getHealth(), health0 - 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), attack0);
	}

	@Test
	public void testHitsAllCharacters() throws HSException {
		Hellfire hellfire = new Hellfire();
		board.data_.placeCardHandCurrentPlayer(hellfire);
		board.data_.getCurrentPlayer().setMana((byte)5);

		Card theCard = board.data_.getCurrentPlayerCardHand(1);
		HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 0, board, null, null);
		assertEquals(board, ret);

		assertEquals(board.data_.getCurrentPlayer().getMana(), 1);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 27);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 27);

		assertEquals(board.data_.getCurrentPlayer().getNumMinions(), 1);
		assertEquals(board.data_.getWaitingPlayer().getNumMinions(), 2);

		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getHealth(), health0 - 3);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), attack0);

		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getHealth(), health0 - 3);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), attack0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getHealth(), health0 - 3);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), attack0);
	}

	@Test
	public void testSpellpower() throws HSException {
		Minion kobold = new KoboldGeomancer();
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, kobold);

		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 0, board, null, null);
		assertEquals(board, ret);

		assertEquals(board.data_.getCurrentPlayer().getMana(), 1);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);

		assertEquals(board.data_.getCurrentPlayer().getNumMinions(), 2);
		assertEquals(board.data_.getWaitingPlayer().getNumMinions(), 2);

		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getHealth(), health0);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), attack0);

		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getHealth(), health0 - 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), attack0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getHealth(), health0 - 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), attack0);
	}

	@Test
	public void testCannotTargetOpponentMinion() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 1, board, null, null);
		assertNull(ret);

		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 3);

		assertEquals(board.data_.getCurrentPlayer().getNumMinions(), 1);
		assertEquals(board.data_.getWaitingPlayer().getNumMinions(), 3);

		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getHealth(), health0);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), attack0);

		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getHealth(), health0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), attack0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getHealth(), health1);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), attack0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(2).getHealth(), health0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(2).getTotalAttack(), attack0);
	}

	@Test
	public void testCannotTargetSelf() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
		assertNull(ret);

		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 3);

		assertEquals(board.data_.getCurrentPlayer().getNumMinions(), 1);
		assertEquals(board.data_.getWaitingPlayer().getNumMinions(), 3);

		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getHealth(), health0);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), attack0);

		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getHealth(), health0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), attack0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getHealth(), health1);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), attack0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(2).getHealth(), health0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(2).getTotalAttack(), attack0);
	}

	@Test
	public void testCannotTargetMinion() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board, null, null);
		assertNull(ret);

		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 3);

		assertEquals(board.data_.getCurrentPlayer().getNumMinions(), 1);
		assertEquals(board.data_.getWaitingPlayer().getNumMinions(), 3);

		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getHealth(), health0);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), attack0);

		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getHealth(), health0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), attack0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getHealth(), health1);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), attack0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(2).getHealth(), health0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(2).getTotalAttack(), attack0);
	}


	@Test
	public void testCannotReuse() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		theCard.hasBeenUsed(true);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board, null, null);
		assertNull(ret);

		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 3);
	}
}
