package com.hearthsim.test.minion;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Beast;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.Houndmaster;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.boardstate.BoardState;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestHoundmaster {


	private HearthTreeNode board;
	private static final byte mana = 2;
	private static final byte attack0 = 2;
	private static final byte health0 = 5;
	private static final byte health1 = 1;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardState());
		Minion minion0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion1 = new Beast("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion2 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion3 = new Beast("" + 0, mana, attack0, health1, attack0, health1, health1);
		Minion minion4 = new Minion("" + 0, mana, attack0, (byte)(health0-2), attack0, health0, health0);

		Houndmaster fb = new Houndmaster();
		board.data_.placeCard_hand_p0(fb);
		board.data_.placeMinion(0, minion0);
		board.data_.placeMinion(0, minion1);
		board.data_.placeMinion(1, minion2);
		board.data_.placeMinion(1, minion3);
		board.data_.placeMinion(1, minion4);
		
		board.data_.setMana_p0(10);
	}
	

	@Test
	public void test_deepCopy() {
		

		Minion card1 = new Houndmaster();
		Minion card1_cloned = (Minion)card1.deepCopy();
		
		assertTrue(card1.equals(card1_cloned));
		assertTrue(card1_cloned.equals(card1));
		
		card1.setHealth((byte)(card1.getHealth() + 1));
		assertFalse(card1.equals(card1_cloned));
		assertFalse(card1_cloned.equals(card1));

		card1_cloned = (Minion)card1.deepCopy();
		assertTrue(card1.equals(card1_cloned));
		assertTrue(card1_cloned.equals(card1));

		card1.setAttack((byte)(card1.getTotalAttack() + 1));
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


		Deck deck = null;
		
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode res;
		Minion target = null;
		
		target = board.data_.getCharacter(1, 0);
		res = theCard.useOn(1, target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(0, 0);
		res = theCard.useOn(0, target, board, deck, null);
		assertFalse(res == null);
		assertTrue(res.data_.getNumCards_hand() == 0);
		assertTrue(res.data_.getNumMinions_p0() == 3);
		assertTrue(res.data_.getNumMinions_p1() == 3);
		assertTrue(res.data_.getMana_p0() == 6);
		
		assertTrue(res.data_.getMinion_p0(0).getHealth() == 3);
		assertTrue(res.data_.getMinion_p0(0).getTotalAttack() == 4);
		assertFalse(res.data_.getMinion_p0(0).getTaunt());
		assertTrue(res.data_.getMinion_p0(1).getHealth() == health0);
		assertTrue(res.data_.getMinion_p0(1).getTotalAttack() == attack0);
		assertFalse(res.data_.getMinion_p0(1).getTaunt());
		assertTrue(res.data_.getMinion_p0(2).getHealth() == health0);
		assertTrue(res.data_.getMinion_p0(2).getTotalAttack() == attack0);
		assertFalse(res.data_.getMinion_p0(2).getTaunt());

		assertTrue(res.data_.getMinion_p1(0).getHealth() == health0);
		assertTrue(res.data_.getMinion_p1(0).getTotalAttack() == attack0);
		assertFalse(res.data_.getMinion_p1(0).getTaunt());
		assertTrue(res.data_.getMinion_p1(1).getHealth() == health1);
		assertTrue(res.data_.getMinion_p1(1).getTotalAttack() == attack0);
		assertFalse(res.data_.getMinion_p1(1).getTaunt());
		assertTrue(res.data_.getMinion_p1(2).getHealth() == health0-2);
		assertTrue(res.data_.getMinion_p1(2).getTotalAttack() == attack0);
		assertFalse(res.data_.getMinion_p1(2).getTaunt());
		
		assertTrue(res.data_.getHero_p0().getHealth() == 30);
		assertTrue(res.data_.getHero_p1().getHealth() == 30);

		assertTrue(res.numChildren() == 1);
		assertTrue(res.getChildren().get(0).data_.getMinion_p0(0).getHealth() == 3);
		assertTrue(res.getChildren().get(0).data_.getMinion_p0(0).getTotalAttack() == 4);
		assertFalse(res.getChildren().get(0).data_.getMinion_p0(0).getTaunt());
		assertTrue(res.getChildren().get(0).data_.getMinion_p0(1).getHealth() == health0);
		assertTrue(res.getChildren().get(0).data_.getMinion_p0(1).getTotalAttack() == attack0);
		assertFalse(res.getChildren().get(0).data_.getMinion_p0(1).getTaunt());
		assertTrue(res.getChildren().get(0).data_.getMinion_p0(2).getHealth() == health0 + 2);
		assertTrue(res.getChildren().get(0).data_.getMinion_p0(2).getTotalAttack() == attack0 + 2);
		assertTrue(res.getChildren().get(0).data_.getMinion_p0(2).getTaunt());
		
		assertTrue(res.getChildren().get(0).data_.getMinion_p1(0).getHealth() == health0);
		assertTrue(res.getChildren().get(0).data_.getMinion_p1(0).getTotalAttack() == attack0);
		assertFalse(res.getChildren().get(0).data_.getMinion_p1(0).getTaunt());
		assertTrue(res.getChildren().get(0).data_.getMinion_p1(1).getHealth() == health1);
		assertTrue(res.getChildren().get(0).data_.getMinion_p1(1).getTotalAttack() == attack0);
		assertFalse(res.getChildren().get(0).data_.getMinion_p1(1).getTaunt());
		assertTrue(res.getChildren().get(0).data_.getMinion_p1(2).getHealth() == health0-2);
		assertTrue(res.getChildren().get(0).data_.getMinion_p1(2).getTotalAttack() == attack0);
		assertFalse(res.getChildren().get(0).data_.getMinion_p1(2).getTaunt());
		
	}


}
