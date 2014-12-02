package com.hearthsim.test.card;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.concrete.Backstab;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestBackstab {

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
		Minion minion3 = new Minion("" + 0, mana, attack0, (byte)(health0-2), attack0, health0, health0);

		Backstab fb = new Backstab();
		board.data_.placeCardHandCurrentPlayer(fb);
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion2);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion3);
		
		board.data_.getCurrentPlayer().setMana(10);
	}
	
	@Test
	public void test0() throws HSException {


		Deck deck = null;
		
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode res;
		Minion target = null;

		target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		res = theCard.getCardAction().useOn(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		res = theCard.getCardAction().useOn(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertTrue(res == null);

		target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 1);
		res = theCard.getCardAction().useOn(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertFalse(res == null);
		assertTrue(res.data_.getNumCards_hand() == 0);
		assertTrue(res.data_.getCurrentPlayer().getNumMinions() == 1);
		assertTrue(res.data_.getWaitingPlayer().getNumMinions() == 3);
		assertTrue(res.data_.getCurrentPlayer().getMana() == 10);
		assertTrue(res.data_.getCurrentPlayer().getMinions().get(0).getHealth() == health0-2);
		assertTrue(res.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack() == attack0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack() == attack0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(1).getHealth() == health1);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack() == attack0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(2).getHealth() == health0-2);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(2).getTotalAttack() == attack0);
		assertTrue(res.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(res.data_.getWaitingPlayerHero().getHealth() == 30);

	}

	@Test
	public void test1() throws HSException {


		Deck deck = null;
		
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode res;
		Minion target = null;
		
		target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 1);
		res = theCard.getCardAction().useOn(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertFalse(res == null);
		assertTrue(res.data_.getNumCards_hand() == 0);
		assertTrue(res.data_.getCurrentPlayer().getNumMinions() == 1);
		assertTrue(res.data_.getWaitingPlayer().getNumMinions() == 3);
		assertTrue(res.data_.getCurrentPlayer().getMana() == 10);
		assertTrue(res.data_.getCurrentPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue(res.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack() == attack0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(0).getHealth() == health0-2);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack() == attack0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(1).getHealth() == health1);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack() == attack0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(2).getHealth() == health0-2);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(2).getTotalAttack() == attack0);
		assertTrue(res.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(res.data_.getWaitingPlayerHero().getHealth() == 30);

	}

	@Test
	public void test2() throws HSException {


		Deck deck = null;
		
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode res;
		Minion target = null;
		
		target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 2);
		res = theCard.getCardAction().useOn(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertFalse(res == null);
		assertTrue(res.data_.getNumCards_hand() == 0);
		assertTrue(res.data_.getCurrentPlayer().getNumMinions() == 1);
		assertTrue(res.data_.getWaitingPlayer().getNumMinions() == 2);
		assertTrue(res.data_.getCurrentPlayer().getMana() == 10);
		assertTrue(res.data_.getCurrentPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue(res.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack() == attack0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack() == attack0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(1).getHealth() == health0-2);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack() == attack0);
		assertTrue(res.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(res.data_.getWaitingPlayerHero().getHealth() == 30);

	}
	
	@Test
	public void test3() throws HSException {


		Deck deck = null;
		
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode res;
		Minion target = null;

		target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 3);
		res = theCard.getCardAction().useOn(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertTrue(res == null);

	}
}
