package com.hearthsim.test.card;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.concrete.Assassinate;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestAssassinate {

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

		Assassinate fb = new Assassinate();
		board.data_.placeCardHandCurrentPlayer(fb);
		board.data_.placeMinion(board.data_.getCurrentPlayer(), minion0);
		board.data_.placeMinion(board.data_.getWaitingPlayer(), minion1);
		board.data_.placeMinion(board.data_.getWaitingPlayer(), minion2);
		board.data_.placeMinion(board.data_.getWaitingPlayer(), minion3);
		
		board.data_.setMana_p0(10);
	}
	
	@Test
	public void test0() throws HSException {

		
		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}
		
		Deck deck = new Deck(cards);
		
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode res;
		Minion target = null;
		
		target = board.data_.getCharacter(board.data_.getWaitingPlayer(), 0);
		res = theCard.useOn(board.data_.getWaitingPlayer(), target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(board.data_.getCurrentPlayer(), 1);
		res = theCard.useOn(board.data_.getCurrentPlayer(), target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(board.data_.getCurrentPlayer(), 0);
		res = theCard.useOn(board.data_.getCurrentPlayer(), target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(board.data_.getWaitingPlayer(), 1);
		res = theCard.useOn(board.data_.getWaitingPlayer(), target, board, deck, null);
		assertFalse(res == null);
		assertTrue(res.data_.getNumCards_hand() == 0);
		assertTrue(res.data_.getCurrentPlayer().getNumMinions() == 1);
		assertTrue(res.data_.getWaitingPlayer().getNumMinions() == 2);
		assertTrue(res.data_.getMana_p0() == 5);
		assertTrue(res.data_.getCurrentPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue(res.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack() == attack0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(0).getHealth() == health1);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack() == attack0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(1).getHealth() == health0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack() == attack0);
		assertTrue(res.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(res.data_.getWaitingPlayerHero().getHealth() == 30);

	}

	@Test
	public void test1() throws HSException {
		
		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}
		
		Deck deck = new Deck(cards);
		
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode res;
		Minion target = null;
		
		target = board.data_.getCharacter(board.data_.getWaitingPlayer(), 0);
		res = theCard.useOn(board.data_.getWaitingPlayer(), target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(board.data_.getCurrentPlayer(), 1);
		res = theCard.useOn(board.data_.getCurrentPlayer(), target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(board.data_.getCurrentPlayer(), 0);
		res = theCard.useOn(board.data_.getCurrentPlayer(), target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(board.data_.getWaitingPlayer(), 2);
		res = theCard.useOn(board.data_.getWaitingPlayer(), target, board, deck, null);
		assertFalse(res == null);
		assertTrue(res.data_.getNumCards_hand() == 0);
		assertTrue(res.data_.getCurrentPlayer().getNumMinions() == 1);
		assertTrue(res.data_.getWaitingPlayer().getNumMinions() == 2);
		assertTrue(res.data_.getMana_p0() == 5);
		assertTrue(res.data_.getCurrentPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue(res.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack() == attack0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack() == attack0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(1).getHealth() == health0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack() == attack0);
		assertTrue(res.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(res.data_.getWaitingPlayerHero().getHealth() == 30);

	}
	
	@Test
	public void test2() throws HSException {

		
		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}
		
		Deck deck = new Deck(cards);
		
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode res;
		Minion target = null;
		
		target = board.data_.getCharacter(board.data_.getWaitingPlayer(), 0);
		res = theCard.useOn(board.data_.getWaitingPlayer(), target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(board.data_.getCurrentPlayer(), 1);
		res = theCard.useOn(board.data_.getCurrentPlayer(), target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(board.data_.getCurrentPlayer(), 0);
		res = theCard.useOn(board.data_.getCurrentPlayer(), target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(board.data_.getWaitingPlayer(), 3);
		res = theCard.useOn(board.data_.getWaitingPlayer(), target, board, deck, null);
		assertFalse(res == null);
		assertTrue(res.data_.getNumCards_hand() == 0);
		assertTrue(res.data_.getCurrentPlayer().getNumMinions() == 1);
		assertTrue(res.data_.getWaitingPlayer().getNumMinions() == 2);
		assertTrue(res.data_.getMana_p0() == 5);
		assertTrue(res.data_.getCurrentPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue(res.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack() == attack0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack() == attack0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(1).getHealth() == health1);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack() == attack0);
		assertTrue(res.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(res.data_.getWaitingPlayerHero().getHealth() == 30);

	}
}
