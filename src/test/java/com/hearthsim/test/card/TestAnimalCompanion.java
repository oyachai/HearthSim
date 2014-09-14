package com.hearthsim.test.card;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.Leokk;
import com.hearthsim.card.spellcard.concrete.AnimalCompanion;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestAnimalCompanion {


	private HearthTreeNode board;
	private Deck deck;
	private static final byte mana = 2;
	private static final byte attack0 = 5;
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

		AnimalCompanion fb = new AnimalCompanion();
		board.data_.placeCardHandCurrentPlayer(fb);

		board.data_.setMana_p0((byte)4);
		board.data_.setMana_p1((byte)4);
		
		board.data_.setMaxMana_p0((byte)4);
		board.data_.setMaxMana_p1((byte)4);
		
	}
	
	@Test
	public void testLeokk0() throws HSException {
		
		Card leokk = new Leokk();
		board.data_.placeCardHandCurrentPlayer(leokk);
		
		Card theCard = board.data_.getCurrentPlayerCardHand(1);
		Minion target = board.data_.getCharacter(board.data_.getWaitingPlayer(), 0);
		HearthTreeNode ret = theCard.useOn(board.data_.getWaitingPlayer(), target, board, deck, null);
		
		assertTrue(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 2);
		assertEquals(board.data_.getCurrentPlayer().getNumMinions(), 2);
		assertEquals(board.data_.getWaitingPlayer().getNumMinions(), 2);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getHealth(), health0);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getHealth(), health1 - 1);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getHealth(), health0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getHealth(), health1 - 1);
	}
	
	@Test
	public void testLeokk1() throws HSException {
		
		Card leokk = new Leokk();
		board.data_.placeCardHandCurrentPlayer(leokk);
		
		Card theCard = board.data_.getCurrentPlayerCardHand(1);
		Minion target = board.data_.getCharacter(board.data_.getCurrentPlayer(), 2);
		HearthTreeNode ret = theCard.useOn(board.data_.getCurrentPlayer(), target, board, deck, null);
		
		//Use Leokk.  The other minions should now be buffed with +1 attack
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getCurrentPlayer().getNumMinions(), 3);
		assertEquals(board.data_.getWaitingPlayer().getNumMinions(), 2);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getHealth(), health0);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getHealth(), health1 - 1);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(2).getHealth(), 4);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getHealth(), health0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getHealth(), health1 - 1);

		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), attack0 + 1);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), attack0 + 1);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), attack0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), attack0);

		
		//Now, attack and kill Leokk.  All minions should go back to their original attack
		Minion minion = board.data_.getCurrentPlayer().getMinions().get(2);
		minion.hasAttacked(false);
		Minion target2 = board.data_.getCharacter(board.data_.getWaitingPlayer(), 1);
		ret = minion.attack(board.data_.getWaitingPlayer(), target2, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getCurrentPlayer().getNumMinions(), 2);
		assertEquals(board.data_.getWaitingPlayer().getNumMinions(), 2);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getHealth(), health0);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getHealth(), health1 - 1);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getHealth(), health0 - 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getHealth(), health1 - 1);

		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), attack0);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), attack0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), attack0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), attack0);

	}
	
	@Test
	public void test0() throws HSException {
		
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		Minion target = board.data_.getCharacter(board.data_.getWaitingPlayer(), 0);
		HearthTreeNode ret = theCard.useOn(board.data_.getWaitingPlayer(), target, board, deck, null);
		
		assertTrue(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getCurrentPlayer().getNumMinions(), 2);
		assertEquals(board.data_.getWaitingPlayer().getNumMinions(), 2);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getHealth(), health0);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getHealth(), health1 - 1);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getHealth(), health0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getHealth(), health1 - 1);
	}

	@Test
	public void test1() throws HSException {
		
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		Minion target = board.data_.getCharacter(board.data_.getCurrentPlayer(), 0);
		HearthTreeNode ret = theCard.useOn(board.data_.getCurrentPlayer(), target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(0, board.data_.getNumCards_hand());
		assertEquals(1, board.data_.getMana_p0());
		assertEquals(3, board.data_.getCurrentPlayer().getNumMinions());
		assertEquals(2, board.data_.getWaitingPlayer().getNumMinions());
		assertEquals(30, board.data_.getCurrentPlayerHero().getHealth());
		assertEquals(30, board.data_.getWaitingPlayerHero().getHealth());
		assertEquals(health0, board.data_.getCurrentPlayer().getMinions().get(0).getHealth());
		assertEquals(health1 - 1, board.data_.getCurrentPlayer().getMinions().get(1).getHealth());
		assertEquals(health0, board.data_.getWaitingPlayer().getMinions().get(0).getHealth());
		assertEquals(health1  - 1, board.data_.getWaitingPlayer().getMinions().get(1).getHealth());

		if (board.data_.getCurrentPlayer().getMinions().get(2) instanceof Leokk) {
			assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), attack0 + 1);
			assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), attack0 + 1);
		} else {
			assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), attack0);
			assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), attack0);
		}
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), attack0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), attack0);

	}
}
