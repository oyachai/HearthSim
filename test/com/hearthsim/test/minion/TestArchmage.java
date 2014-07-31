package com.hearthsim.test.minion;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.Archmage;
import com.hearthsim.card.spellcard.concrete.HolySmite;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestArchmage {
	
	private HearthTreeNode board;
	private Deck deck;
	private static final byte mana = 2;
	private static final byte attack0 = 8;
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

		Card am = new Archmage();
		board.data_.placeCard_hand_p0(am);

		Card hs = new HolySmite();
		board.data_.placeCard_hand_p0(hs);

		board.data_.setMana_p0((byte)8);
		board.data_.setMana_p1((byte)8);
		
		board.data_.setMaxMana_p0((byte)8);
		board.data_.setMaxMana_p1((byte)8);
		
	}
	
	@Test
	public void test_deepCopy() {
		

		Minion card1 = new Archmage();
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
	public void test0() throws HSException {
		
		Minion target = board.data_.getCharacter(1, 0);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(1, target, board, deck, null);
		
		assertTrue(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 2);
		assertEquals(board.data_.getNumMinions_p0(), 2);
		assertEquals(board.data_.getNumMinions_p1(), 2);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), health0);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), health1 - 1);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), health0);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), health1 - 1);
	}

	@Test
	public void test1() throws HSException {
		
		Minion target = board.data_.getCharacter(0, 2);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(0, target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getNumMinions_p0(), 3);
		assertEquals(board.data_.getNumMinions_p1(), 2);
		assertEquals(board.data_.getMana_p0(), 2);
		assertEquals(board.data_.getMana_p1(), 8);		
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), health0);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), health1 - 1);
		assertEquals(board.data_.getMinion_p0(2).getHealth(), 7);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), health0);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), health1 - 1);
		
		assertEquals(attack0, board.data_.getMinion_p0(0).getAttack(), attack0);
		assertEquals(attack0, board.data_.getMinion_p0(1).getAttack(), attack0);
		assertEquals(attack0, board.data_.getMinion_p0(2).getAttack(), 4);
		assertEquals(attack0, board.data_.getMinion_p1(0).getAttack(), attack0);
		assertEquals(attack0, board.data_.getMinion_p1(1).getAttack(), attack0);
		
		assertEquals(1, board.data_.getSpellDamage(0));
		
		target = board.data_.getCharacter(1, 0);
		theCard = board.data_.getCard_hand_p0(0);
		ret = theCard.useOn(1, target, board, deck, null);

		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getNumMinions_p0(), 3);
		assertEquals(board.data_.getNumMinions_p1(), 2);
		assertEquals(30, board.data_.getHero_p0().getHealth());
		assertEquals(27, board.data_.getHero_p1().getHealth());
		assertEquals(board.data_.getMana_p0(), 1);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), health0);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), health1 - 1);
		assertEquals(board.data_.getMinion_p0(2).getHealth(), 7);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), health0);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), health1 - 1);
		
		assertEquals(attack0, board.data_.getMinion_p0(0).getAttack(), attack0);
		assertEquals(attack0, board.data_.getMinion_p0(1).getAttack(), attack0);
		assertEquals(attack0, board.data_.getMinion_p0(2).getAttack(), 4);
		assertEquals(attack0, board.data_.getMinion_p1(0).getAttack(), attack0);
		assertEquals(attack0, board.data_.getMinion_p1(1).getAttack(), attack0);

	}
	
	@Test
	public void test2() throws HSException {
		
		Minion target = board.data_.getCharacter(0, 2);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(0, target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getNumMinions_p0(), 3);
		assertEquals(board.data_.getNumMinions_p1(), 2);
		assertEquals(board.data_.getMana_p0(), 2);
		assertEquals(board.data_.getMana_p1(), 8);		
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), health0);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), health1 - 1);
		assertEquals(board.data_.getMinion_p0(2).getHealth(), 7);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), health0);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), health1 - 1);
		
		assertEquals(attack0, board.data_.getMinion_p0(0).getAttack(), attack0);
		assertEquals(attack0, board.data_.getMinion_p0(1).getAttack(), attack0);
		assertEquals(attack0, board.data_.getMinion_p0(2).getAttack(), 4);
		assertEquals(attack0, board.data_.getMinion_p1(0).getAttack(), attack0);
		assertEquals(attack0, board.data_.getMinion_p1(1).getAttack(), attack0);
		
		assertEquals(1, board.data_.getSpellDamage(0));
		
		target = board.data_.getCharacter(1, 1);
		theCard = board.data_.getCard_hand_p0(0);
		ret = theCard.useOn(1, target, board, deck, null);

		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(3, board.data_.getNumMinions_p0());
		assertEquals(1, board.data_.getNumMinions_p1());
		assertEquals(30, board.data_.getHero_p0().getHealth());
		assertEquals(30, board.data_.getHero_p1().getHealth());
		assertEquals(board.data_.getMana_p0(), 1);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), health0);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), health1 - 1);
		assertEquals(board.data_.getMinion_p0(2).getHealth(), 7);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), health1 - 1);
		
		assertEquals(attack0, board.data_.getMinion_p0(0).getAttack(), attack0);
		assertEquals(attack0, board.data_.getMinion_p0(1).getAttack(), attack0);
		assertEquals(attack0, board.data_.getMinion_p0(2).getAttack(), 4);
		assertEquals(attack0, board.data_.getMinion_p1(0).getAttack(), attack0);
	}

}
