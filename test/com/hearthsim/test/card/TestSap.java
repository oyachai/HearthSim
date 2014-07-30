package com.hearthsim.test.card;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.spellcard.concrete.Sap;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestSap {


	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() {
		board = new HearthTreeNode(new BoardState());

		Minion minion0_0 = new BloodfenRaptor();
		Minion minion0_1 = new RaidLeader();
		Minion minion1_0 = new BloodfenRaptor();
		Minion minion1_1 = new RaidLeader();
		
		board.data_.placeCard_hand_p0(minion0_0);
		board.data_.placeCard_hand_p0(minion0_1);
				
		board.data_.placeCard_hand_p1(minion1_0);
		board.data_.placeCard_hand_p1(minion1_1);

		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}
	
		deck = new Deck(cards);

		Card fb = new Sap();
		board.data_.placeCard_hand_p0(fb);

		board.data_.setMana_p0((byte)10);
		board.data_.setMana_p1((byte)10);
		
		board.data_.setMaxMana_p0((byte)10);
		board.data_.setMaxMana_p1((byte)10);
		
		HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
		try {
			tmpBoard.data_.getCard_hand_p0(0).useOn(0, tmpBoard.data_.getHero_p0(), tmpBoard, deck, null);
			tmpBoard.data_.getCard_hand_p0(0).useOn(0, tmpBoard.data_.getHero_p0(), tmpBoard, deck, null);
		} catch (HSInvalidPlayerIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
		try {
			board.data_.getCard_hand_p0(0).useOn(0, board.data_.getHero_p0(), board, deck, null);
			board.data_.getCard_hand_p0(0).useOn(0, board.data_.getHero_p0(), board, deck, null);
		} catch (HSInvalidPlayerIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board.data_.resetMana();
		board.data_.resetMinions();
	}
	
	@Test
	public void test0() throws HSInvalidPlayerIndexException {
		
		Minion target = board.data_.getCharacter(0, 0);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(0, target, board, deck, null);
		
		assertTrue(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getNumMinions_p0(), 2);
		assertEquals(board.data_.getNumMinions_p1(), 2);
		assertEquals(board.data_.getMana_p0(), 10);
		assertEquals(board.data_.getMana_p1(), 10);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), 2);

		assertEquals(board.data_.getMinion_p0(0).getAttack(), 2);
		assertEquals(board.data_.getMinion_p0(1).getAttack(), 4);
		assertEquals(board.data_.getMinion_p1(0).getAttack(), 2);
		assertEquals(board.data_.getMinion_p1(1).getAttack(), 4);
	}

	@Test
	public void test1() throws HSInvalidPlayerIndexException {
		
		Minion target = board.data_.getCharacter(1, 0);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(1, target, board, deck, null);
		
		assertTrue(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getNumMinions_p0(), 2);
		assertEquals(board.data_.getNumMinions_p1(), 2);
		assertEquals(board.data_.getMana_p0(), 10);
		assertEquals(board.data_.getMana_p1(), 10);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), 2);

		assertEquals(board.data_.getMinion_p0(0).getAttack(), 2);
		assertEquals(board.data_.getMinion_p0(1).getAttack(), 4);
		assertEquals(board.data_.getMinion_p1(0).getAttack(), 2);
		assertEquals(board.data_.getMinion_p1(1).getAttack(), 4);
	}
	
	@Test
	public void test2() throws HSInvalidPlayerIndexException {
		
		Minion target = board.data_.getCharacter(1, 1);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(1, target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getNumCards_hand_p1(), 1);
		assertEquals(board.data_.getNumMinions_p0(), 2);
		assertEquals(board.data_.getNumMinions_p1(), 1);
		assertEquals(board.data_.getMana_p0(), 8);
		assertEquals(board.data_.getMana_p1(), 10);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 2);

		assertEquals(board.data_.getMinion_p0(0).getAttack(), 2);
		assertEquals(board.data_.getMinion_p0(1).getAttack(), 4);
		assertEquals(board.data_.getMinion_p1(0).getAttack(), 3);
		
		assertTrue(board.data_.getCard_hand_p1(0) instanceof RaidLeader);
	}

	@Test
	public void test3() throws HSInvalidPlayerIndexException {
		
		Minion target = board.data_.getCharacter(1, 2);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(1, target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getNumCards_hand_p1(), 1);
		assertEquals(board.data_.getNumMinions_p0(), 2);
		assertEquals(board.data_.getNumMinions_p1(), 1);
		assertEquals(board.data_.getMana_p0(), 8);
		assertEquals(board.data_.getMana_p1(), 10);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 2);

		assertEquals(board.data_.getMinion_p0(0).getAttack(), 2);
		assertEquals(board.data_.getMinion_p0(1).getAttack(), 4);
		assertEquals(board.data_.getMinion_p1(0).getAttack(), 2);

		assertTrue(board.data_.getCard_hand_p1(0) instanceof BloodfenRaptor);
	}

	@Test
	public void test4() throws HSInvalidPlayerIndexException {
		
		Minion target = board.data_.getCharacter(1, 2);
		for (int indx = 0; indx < 10; ++indx) 
			board.data_.placeCard_hand_p1(new TheCoin());
		
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(1, target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getNumCards_hand_p1(), 10);
		assertEquals(board.data_.getNumMinions_p0(), 2);
		assertEquals(board.data_.getNumMinions_p1(), 1);
		assertEquals(board.data_.getMana_p0(), 8);
		assertEquals(board.data_.getMana_p1(), 10);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 2);

		assertEquals(board.data_.getMinion_p0(0).getAttack(), 2);
		assertEquals(board.data_.getMinion_p0(1).getAttack(), 4);
		assertEquals(board.data_.getMinion_p1(0).getAttack(), 2);

		for (int indx = 0; indx < 10; ++indx) 
			assertTrue(board.data_.getCard_hand_p1(0) instanceof TheCoin);

	}

}
