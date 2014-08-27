package com.hearthsim.test.minion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.AngryChicken;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.DustDevil;
import com.hearthsim.card.minion.concrete.GoldshireFootman;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.boardstate.BoardState;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestDustDevil {

	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardState());

		Minion minion0_0 = new GoldshireFootman();
		Minion minion0_1 = new GoldshireFootman();
		Minion minion1_0 = new AngryChicken();
		Minion minion1_1 = new AngryChicken();
		
		board.data_.placeMinion(0, minion0_0);
		board.data_.placeMinion(0, minion0_1);
		
		board.data_.placeMinion(1, minion1_0);
		board.data_.placeMinion(1, minion1_1);
		
		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new BloodfenRaptor();
		}
	
		deck = new Deck(cards);

		Minion fb = new DustDevil();
		board.data_.placeCard_hand_p0(fb);

		board.data_.setMana_p0((byte)7);
		board.data_.setMana_p1((byte)7);
		
		board.data_.setMaxMana_p0((byte)7);
		board.data_.setMaxMana_p1((byte)7);
		
	}
	
	@Test
	public void test0() throws HSException {
		
		Minion target = board.data_.getCharacter(1, 0);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(1, target, board, deck, null);
		
		assertTrue(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getNumMinions_p0(), 2);
		assertEquals(board.data_.getNumMinions_p1(), 2);
		assertEquals(board.data_.getMana_p0(), 7);
		assertEquals(board.data_.getMana_p1(), 7);
		assertEquals(board.data_.getHero_p0().getTotalHealth(), 30);
		assertEquals(board.data_.getHero_p1().getTotalHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getTotalHealth(), 2);
		assertEquals(board.data_.getMinion_p0(1).getTotalHealth(), 2);
		assertEquals(board.data_.getMinion_p1(0).getTotalHealth(), 1);
		assertEquals(board.data_.getMinion_p1(1).getTotalHealth(), 1);
	}
	

	@Test
	public void test2() throws HSException {
		
		Minion target = board.data_.getCharacter(0, 2);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(0, target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getNumMinions_p0(), 3);
		assertEquals(board.data_.getNumMinions_p1(), 2);
		
		assertEquals(board.data_.getMana_p0(), 6);
		assertEquals(board.data_.getMana_p1(), 7);
		
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getTotalHealth(), 2);
		assertEquals(board.data_.getMinion_p0(1).getTotalHealth(), 2);
		assertEquals(board.data_.getMinion_p0(2).getTotalHealth(), 1);
		assertEquals(board.data_.getMinion_p1(0).getTotalHealth(), 1);
		assertEquals(board.data_.getMinion_p1(1).getTotalHealth(), 1);
		
		assertEquals(board.data_.getMinion_p0(0).getTotalAttack(), 1);
		assertEquals(board.data_.getMinion_p0(1).getTotalAttack(), 1);
		assertEquals(board.data_.getMinion_p0(2).getTotalAttack(), 3);
		assertEquals(board.data_.getMinion_p1(0).getTotalAttack(), 1);
		assertEquals(board.data_.getMinion_p1(1).getTotalAttack(), 1);
		
		//overloaded for 2, so when resetMana is called, it should set the mana to 5
		board.data_.resetMana();
		assertEquals(board.data_.getMana_p0(), 5);
		assertEquals(board.data_.getMana_p1(), 7);
		
	}
}
