package com.hearthsim.test.card;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.concrete.Execute;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestExecute {


	private HearthTreeNode board;
	private Deck deck;
	private static final byte mana = 2;
	private static final byte attack0 = 2;
	private static final byte health0 = 3;
	private static final byte health1 = 7;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardState());

		Minion minion0_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion0_1 = new Minion("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);
		Minion minion1_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion1_1 = new Minion("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);
		
		board.data_.placeMinion(0, minion0_0);
		board.data_.placeMinion(0, minion0_1);
		
		board.data_.placeMinion(1, minion1_0);
		board.data_.placeMinion(1, minion1_1);
		
		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}
	
		deck = new Deck(cards);
	}
	
	@Test
	public void test0() throws HSException {
		Execute fb = new Execute();
		board.data_.placeCard_hand_p0(fb);
		
		Minion target = board.data_.getCharacter(0, 0);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(0, target, board, deck, null);
		assertTrue(ret == null);
		assertTrue(board.data_.getNumCards_hand() == 1);
		assertTrue(board.data_.getNumMinions_p0() == 2);
		assertTrue(board.data_.getNumMinions_p1() == 2);
		assertTrue(board.data_.getHero_p0().getHealth() == 30);
		assertTrue(board.data_.getHero_p1().getHealth() == 30);
		assertTrue(board.data_.getMinion_p0(0).getHealth() == health0);
		assertTrue(board.data_.getMinion_p0(1).getHealth() == health1 - 1);
		assertTrue(board.data_.getMinion_p1(0).getHealth() == health0);
		assertTrue(board.data_.getMinion_p1(1).getHealth() == health1 - 1);
		assertTrue(board.data_.getMinion_p0(0).getTotalAttack() == attack0);
		assertTrue(board.data_.getMinion_p0(1).getTotalAttack() == attack0);
		assertTrue(board.data_.getMinion_p1(0).getTotalAttack() == attack0);
		assertTrue(board.data_.getMinion_p1(1).getTotalAttack() == attack0);
		assertFalse(board.data_.getMinion_p0(0).getCharge());
		assertFalse(board.data_.getMinion_p0(1).getCharge());
	}

	@Test
	public void test1() throws HSException {
		Execute fb = new Execute();
		board.data_.placeCard_hand_p0(fb);
		
		Minion target = board.data_.getCharacter(1, 1);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(1, target, board, deck, null);
		assertTrue(ret == null);
		assertTrue(board.data_.getNumCards_hand() == 1);
		assertTrue(board.data_.getNumMinions_p0() == 2);
		assertTrue(board.data_.getNumMinions_p1() == 2);
		assertTrue(board.data_.getHero_p0().getHealth() == 30);
		assertTrue(board.data_.getHero_p1().getHealth() == 30);
		assertTrue(board.data_.getMinion_p0(0).getHealth() == health0);
		assertTrue(board.data_.getMinion_p0(1).getHealth() == health1 - 1);
		assertTrue(board.data_.getMinion_p1(0).getHealth() == health0);
		assertTrue(board.data_.getMinion_p1(1).getHealth() == health1 - 1);
		assertTrue(board.data_.getMinion_p0(0).getTotalAttack() == attack0);
		assertTrue(board.data_.getMinion_p0(1).getTotalAttack() == attack0);
		assertTrue(board.data_.getMinion_p1(0).getTotalAttack() == attack0);
		assertTrue(board.data_.getMinion_p1(1).getTotalAttack() == attack0);
		assertFalse(board.data_.getMinion_p0(0).getCharge());
		assertFalse(board.data_.getMinion_p0(1).getCharge());
	}
	
	@Test
	public void test2() throws HSException {
		Execute fb = new Execute();
		board.data_.placeCard_hand_p0(fb);
		
		Minion target = board.data_.getCharacter(1, 2);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(1, target, board, deck, null);
		assertFalse(ret == null);
		assertTrue(board.data_.getNumCards_hand() == 0);
		assertTrue(board.data_.getNumMinions_p0() == 2);
		assertTrue(board.data_.getNumMinions_p1() == 1);
		assertTrue(board.data_.getHero_p0().getHealth() == 30);
		assertTrue(board.data_.getHero_p1().getHealth() == 30);
		assertTrue(board.data_.getMinion_p0(0).getHealth() == health0);
		assertTrue(board.data_.getMinion_p0(1).getHealth() == health1 - 1);
		assertTrue(board.data_.getMinion_p1(0).getHealth() == health0);
		assertTrue(board.data_.getMinion_p0(0).getTotalAttack() == attack0);
		assertTrue(board.data_.getMinion_p0(1).getTotalAttack() == attack0);
		assertTrue(board.data_.getMinion_p1(0).getTotalAttack() == attack0);
		assertFalse(board.data_.getMinion_p0(0).getCharge());
		assertFalse(board.data_.getMinion_p0(1).getCharge());
	}
}
