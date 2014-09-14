package com.hearthsim.test.card;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.concrete.FrostShock;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestFrostShock {

	private HearthTreeNode board;
	private Deck deck;
	private static final byte mana = 2;
	private static final byte attack0 = 2;
	private static final byte health0 = 3;
	private static final byte health1 = 7;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel());

		Minion minion0_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion0_1 = new Minion("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);
		Minion minion1_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion1_1 = new Minion("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);
		
		board.data_.placeMinion(board.data_.getCurrentPlayer(), minion0_0);
		board.data_.placeMinion(board.data_.getCurrentPlayer(), minion0_1);
		
		board.data_.placeMinion(board.data_.getWaitingPlayer(), minion1_0);
		board.data_.placeMinion(board.data_.getWaitingPlayer(), minion1_1);
		
		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}
	
		deck = new Deck(cards);
	}
	
	@Test
	public void test0() throws HSException {
		FrostShock fb = new FrostShock();
		board.data_.placeCardHandCurrentPlayer(fb);
		
		Minion target = board.data_.getCharacter(board.data_.getCurrentPlayer(), 0);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(board.data_.getCurrentPlayer(), target, board, deck, null);
		assertTrue(ret == null);
		assertTrue(board.data_.getNumCards_hand() == 1);
		assertTrue(board.data_.getCurrentPlayer().getNumMinions() == 2);
		assertTrue(board.data_.getWaitingPlayer().getNumMinions() == 2);
		assertTrue(board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getWaitingPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(1).getHealth() == health1 - 1);
		assertTrue(board.data_.getWaitingPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue(board.data_.getWaitingPlayer().getMinions().get(1).getHealth() == health1 - 1);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack() == attack0);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack() == attack0);
		assertTrue(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack() == attack0);
		assertTrue(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack() == attack0);
		assertFalse(board.data_.getCurrentPlayer().getMinions().get(0).getCharge());
		assertFalse(board.data_.getCurrentPlayer().getMinions().get(1).getCharge());
		assertFalse(board.data_.getCurrentPlayer().getMinions().get(0).getFrozen());
		assertFalse(board.data_.getWaitingPlayer().getMinions().get(0).getFrozen());
	}

	@Test
	public void test1() throws HSException {
		FrostShock fb = new FrostShock();
		board.data_.placeCardHandCurrentPlayer(fb);
		
		Minion target = board.data_.getCharacter(board.data_.getWaitingPlayer(), 1);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(board.data_.getWaitingPlayer(), target, board, deck, null);
		assertFalse(ret == null);
		assertTrue(board.data_.getNumCards_hand() == 0);
		assertTrue(board.data_.getCurrentPlayer().getNumMinions() == 2);
		assertTrue(board.data_.getWaitingPlayer().getNumMinions() == 2);
		assertTrue(board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getWaitingPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(1).getHealth() == health1 - 1);
		assertTrue(board.data_.getWaitingPlayer().getMinions().get(0).getHealth() == health0 - 1);
		assertTrue(board.data_.getWaitingPlayer().getMinions().get(1).getHealth() == health1 - 1);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack() == attack0);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack() == attack0);
		assertTrue(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack() == attack0);
		assertTrue(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack() == attack0);
		assertFalse(board.data_.getCurrentPlayer().getMinions().get(0).getCharge());
		assertFalse(board.data_.getCurrentPlayer().getMinions().get(1).getCharge());
		assertFalse(board.data_.getCurrentPlayer().getMinions().get(0).getFrozen());
		assertTrue(board.data_.getWaitingPlayer().getMinions().get(0).getFrozen());
	}

}
