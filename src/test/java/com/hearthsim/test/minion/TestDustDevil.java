package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.AngryChicken;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.DustDevil;
import com.hearthsim.card.minion.concrete.GoldshireFootman;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestDustDevil {

	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel());

		Minion minion0_0 = new GoldshireFootman();
		Minion minion0_1 = new GoldshireFootman();
		Minion minion1_0 = new AngryChicken();
		Minion minion1_1 = new AngryChicken();
		
		board.data_.placeMinion(board.data_.getCurrentPlayer(), minion0_0);
		board.data_.placeMinion(board.data_.getCurrentPlayer(), minion0_1);
		
		board.data_.placeMinion(board.data_.getWaitingPlayer(), minion1_0);
		board.data_.placeMinion(board.data_.getWaitingPlayer(), minion1_1);
		
		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new BloodfenRaptor();
		}
	
		deck = new Deck(cards);

		Minion fb = new DustDevil();
		board.data_.placeCardHandCurrentPlayer(fb);

		board.data_.setMana_p0((byte)7);
		board.data_.setMana_p1((byte)7);
		
		board.data_.setMaxMana_p0((byte)7);
		board.data_.setMaxMana_p1((byte)7);
		
	}
	
	@Test
	public void test0() throws HSException {
		
		Minion target = board.data_.getCharacter(board.data_.getWaitingPlayer(), 0);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(board.data_.getWaitingPlayer(), target, board, deck, null);
		
		assertTrue(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getCurrentPlayer().getNumMinions(), 2);
		assertEquals(board.data_.getWaitingPlayer().getNumMinions(), 2);
		assertEquals(board.data_.getMana_p0(), 7);
		assertEquals(board.data_.getMana_p1(), 7);
		assertEquals(board.data_.getCurrentPlayerHero().getTotalHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getTotalHealth(), 30);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalHealth(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getTotalHealth(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalHealth(), 1);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalHealth(), 1);
	}
	

	@Test
	public void test2() throws HSException {
		
		Minion target = board.data_.getCharacter(board.data_.getCurrentPlayer(), 2);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(board.data_.getCurrentPlayer(), target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getCurrentPlayer().getNumMinions(), 3);
		assertEquals(board.data_.getWaitingPlayer().getNumMinions(), 2);
		
		assertEquals(board.data_.getMana_p0(), 6);
		assertEquals(board.data_.getMana_p1(), 7);
		
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalHealth(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getTotalHealth(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(2).getTotalHealth(), 1);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalHealth(), 1);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalHealth(), 1);
		
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 3);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 1);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 1);
		
		//overloaded for 2, so when resetMana is called, it should set the mana to 5
		board.data_.resetMana();
		assertEquals(board.data_.getMana_p0(), 5);
		assertEquals(board.data_.getMana_p1(), 7);
		
	}
}
