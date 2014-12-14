package com.hearthsim.test.minion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.minion.concrete.WaterElemental;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestWaterElemental {

	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel());

		Card fb = new WaterElemental();
		board.data_.placeCardHandCurrentPlayer(fb);

		board.data_.getCurrentPlayer().setMana((byte)8);
		board.data_.getCurrentPlayer().setMaxMana((byte)8);

		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RaidLeader());
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new BoulderfistOgre());
	}

	@Test
	public void testFreezesMinionOnAttack() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, deck, null);
		assertEquals(board, ret);

		Minion waterElemental = PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0);
		assertTrue(waterElemental instanceof WaterElemental);

		waterElemental.hasAttacked(false); // unset summoning sickness
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 2);
		waterElemental.attack(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getFrozen());
	}


	@Test
	public void testFreezesHeroOnAttack() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, deck, null);
		assertEquals(board, ret);

		Minion waterElemental = PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0);
		assertTrue(waterElemental instanceof WaterElemental);

		waterElemental.hasAttacked(false); // unset summoning sickness
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		waterElemental.attack(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertTrue(board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0).getFrozen());
	}
}
