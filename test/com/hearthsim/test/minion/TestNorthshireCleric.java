package com.hearthsim.test.minion;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.NorthshireCleric;
import com.hearthsim.card.spellcard.concrete.AncestralHealing;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestNorthshireCleric {

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
	public void test_deepCopy() {
		

		Minion card1 = new NorthshireCleric();
		Minion card1_cloned = (Minion)card1.deepCopy();
		
		assertTrue(card1.equals(card1_cloned));
		assertTrue(card1_cloned.equals(card1));
		
		card1.setHealth((byte)(card1.getHealth() + 1));
		assertFalse(card1.equals(card1_cloned));
		assertFalse(card1_cloned.equals(card1));

		card1_cloned = (Minion)card1.deepCopy();
		assertTrue(card1.equals(card1_cloned));
		assertTrue(card1_cloned.equals(card1));

		card1.setAttack((byte)(card1.getAttack() + 1));
		assertFalse(card1.equals(card1_cloned));
		assertFalse(card1_cloned.equals(card1));

		card1_cloned = (Minion)card1.deepCopy();
		assertTrue(card1.equals(card1_cloned));
		assertTrue(card1_cloned.equals(card1));
		
		card1.setDestroyOnTurnEnd(true);
		assertFalse(card1.equals(card1_cloned));
		assertFalse(card1_cloned.equals(card1));
		card1_cloned = (Minion)card1.deepCopy();
		assertTrue(card1.equals(card1_cloned));
		assertTrue(card1_cloned.equals(card1));

		card1.setDestroyOnTurnStart(true);
		assertFalse(card1.equals(card1_cloned));
		assertFalse(card1_cloned.equals(card1));
		card1_cloned = (Minion)card1.deepCopy();
		assertTrue(card1.equals(card1_cloned));
		assertTrue(card1_cloned.equals(card1));

	}
	
	@Test
	public void test0() throws HSInvalidPlayerIndexException {
		NorthshireCleric fb = new NorthshireCleric();
		board.data_.placeCard_hand_p0(fb);
		
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(0, 0, 0, board, deck);
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
	}
	
	@Test
	public void test1() throws HSInvalidPlayerIndexException {
		NorthshireCleric fb = new NorthshireCleric();
		board.data_.placeCard_hand_p0(fb);
		
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(0, 0, 1, board, deck);
		assertFalse(ret == null);

		assertTrue(ret.data_.getNumCards_hand() == 0);
		assertTrue(ret.data_.getNumMinions_p0() == 3);
		assertTrue(ret.data_.getNumMinions_p1() == 2);
		assertTrue(ret.data_.getHero_p0().getHealth() == 30);
		assertTrue(ret.data_.getHero_p1().getHealth() == 30);
		assertTrue(ret.data_.getMinion_p0(0).getHealth() == 3);
		assertTrue(ret.data_.getMinion_p0(1).getHealth() == health0);
		assertTrue(ret.data_.getMinion_p0(2).getHealth() == health1 - 1);
		assertTrue(ret.data_.getMinion_p1(0).getHealth() == health0);
		assertTrue(ret.data_.getMinion_p1(1).getHealth() == health1 - 1);
		
		
		
		AncestralHealing ah = new AncestralHealing();
		board.data_.placeCard_hand_p0(ah);
		theCard = board.data_.getCard_hand_p0(0);
		ret = theCard.useOn(0, 0, 1, board, deck);
		
		assertFalse(ret == null);
		assertTrue(ret.data_.getNumCards_hand() == 0);
		assertTrue(ret.data_.getNumMinions_p0() == 3);
		assertTrue(ret.data_.getNumMinions_p1() == 2);
		assertTrue(ret.data_.getHero_p0().getHealth() == 30);
		assertTrue(ret.data_.getHero_p1().getHealth() == 30);
		assertTrue(ret.data_.getMinion_p0(0).getHealth() == 3);
		assertTrue(ret.data_.getMinion_p0(1).getHealth() == health0);
		assertTrue(ret.data_.getMinion_p0(2).getHealth() == health1 - 1);
		assertTrue(ret.data_.getMinion_p1(0).getHealth() == health0);
		assertTrue(ret.data_.getMinion_p1(1).getHealth() == health1 - 1);
		
		
		
		
		ah = new AncestralHealing();
		board.data_.placeCard_hand_p0(ah);
		theCard = board.data_.getCard_hand_p0(0);
		ret = theCard.useOn(0, 0, 3, board, deck);
		
		assertFalse(ret == null);
		assertTrue(ret.data_.getNumCards_hand() == 1);  //Northshire Cleric should have drawn a card, so 1 card now
		assertTrue(ret.data_.getNumMinions_p0() == 3);
		assertTrue(ret.data_.getNumMinions_p1() == 2);
		assertTrue(ret.data_.getHero_p0().getHealth() == 30);
		assertTrue(ret.data_.getHero_p1().getHealth() == 30);
		assertTrue(ret.data_.getMinion_p0(0).getHealth() == 3);
		assertTrue(ret.data_.getMinion_p0(1).getHealth() == health0);
		assertTrue(ret.data_.getMinion_p0(2).getHealth() == health1);
		assertTrue(ret.data_.getMinion_p1(0).getHealth() == health0);
		assertTrue(ret.data_.getMinion_p1(1).getHealth() == health1 - 1);
		
	}
	
	@Test
	public void test2() throws HSInvalidPlayerIndexException {
		NorthshireCleric fb1 = new NorthshireCleric();
		NorthshireCleric fb2 = new NorthshireCleric();

		board.data_.placeCard_hand_p0(fb1);
		board.data_.placeCard_hand_p0(fb2);
		
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(0, 0, 3, board, deck);
		
		theCard = board.data_.getCard_hand_p0(0);
		ret = theCard.useOn(0, 0, 4, board, deck);
		assertFalse(ret == null);

		assertTrue(ret.data_.getNumCards_hand() == 0);
		assertTrue(ret.data_.getNumMinions_p0() == 4);
		assertTrue(ret.data_.getNumMinions_p1() == 2);
		assertTrue(ret.data_.getHero_p0().getHealth() == 30);
		assertTrue(ret.data_.getHero_p1().getHealth() == 30);
		assertTrue(ret.data_.getMinion_p0(0).getHealth() == health0);
		assertTrue(ret.data_.getMinion_p0(1).getHealth() == health1 - 1);
		assertTrue(ret.data_.getMinion_p0(2).getHealth() == 3);
		assertTrue(ret.data_.getMinion_p0(3).getHealth() == 3);
		assertTrue(ret.data_.getMinion_p1(0).getHealth() == health0);
		assertTrue(ret.data_.getMinion_p1(1).getHealth() == health1 - 1);
		
		
		AncestralHealing ah = new AncestralHealing();
		board.data_.placeCard_hand_p0(ah);
		theCard = board.data_.getCard_hand_p0(0);
		ret = theCard.useOn(0, 0, 2, board, deck);
		
		assertFalse(ret == null);
		assertTrue(ret.data_.getNumCards_hand() == 2); //Two clerics, one heal means 2 new cards
		assertTrue(ret.data_.getNumMinions_p0() == 4);
		assertTrue(ret.data_.getNumMinions_p1() == 2);
		assertTrue(ret.data_.getHero_p0().getHealth() == 30);
		assertTrue(ret.data_.getHero_p1().getHealth() == 30);
		assertTrue(ret.data_.getMinion_p0(0).getHealth() == health0);
		assertTrue(ret.data_.getMinion_p0(1).getHealth() == health1);
		assertTrue(ret.data_.getMinion_p0(2).getHealth() == 3);
		assertTrue(ret.data_.getMinion_p0(3).getHealth() == 3);
		assertTrue(ret.data_.getMinion_p1(0).getHealth() == health0);
		assertTrue(ret.data_.getMinion_p1(1).getHealth() == health1 - 1);
		
	}
}
