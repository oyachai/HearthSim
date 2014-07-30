package com.hearthsim.test.minion;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.Abomination;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.minion.concrete.ScarletCrusader;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestAbomination {

	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() {
		board = new HearthTreeNode(new BoardState());

		Minion minion0_0 = new BoulderfistOgre();
		Minion minion0_1 = new RaidLeader();
		Minion minion1_0 = new BoulderfistOgre();
		Minion minion1_1 = new RaidLeader();
		Minion minion1_2 = new ScarletCrusader();
		
		board.data_.placeCard_hand_p0(minion0_0);
		board.data_.placeCard_hand_p0(minion0_1);
				
		board.data_.placeCard_hand_p1(minion1_0);
		board.data_.placeCard_hand_p1(minion1_1);
		board.data_.placeCard_hand_p1(minion1_2);

		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}
	
		deck = new Deck(cards);

		Card fb = new Abomination();
		board.data_.placeCard_hand_p0(fb);

		board.data_.setMana_p0((byte)8);
		board.data_.setMana_p1((byte)8);
		
		board.data_.setMaxMana_p0((byte)8);
		board.data_.setMaxMana_p1((byte)8);
		
		HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
		try {
			tmpBoard.data_.getCard_hand_p0(0).useOn(0, tmpBoard.data_.getHero_p0(), tmpBoard, deck, null);
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
		
		//null case
		Minion target = board.data_.getCharacter(1, 0);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(1, target, board, deck, null);
		
		assertTrue(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getNumMinions_p0(), 2);
		assertEquals(board.data_.getNumMinions_p1(), 3);
		assertEquals(board.data_.getMana_p0(), 8);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 7);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 1);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(2).getHealth(), 7);

		assertEquals(board.data_.getMinion_p0(0).getAttack(), 2);
		assertEquals(board.data_.getMinion_p0(1).getAttack(), 7);
		assertEquals(board.data_.getMinion_p1(0).getAttack(), 4);
		assertEquals(board.data_.getMinion_p1(1).getAttack(), 2);
		assertEquals(board.data_.getMinion_p1(2).getAttack(), 7);
		
		assertTrue(board.data_.getMinion_p1(0).getDivineShield());
	}

	@Test
	public void test1() throws HSInvalidPlayerIndexException {
		
		//null case
		Minion target = board.data_.getCharacter(0, 0);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(0, target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getNumMinions_p0(), 3);
		assertEquals(board.data_.getNumMinions_p1(), 3);
		assertEquals(board.data_.getMana_p0(), 3);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 4);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(2).getHealth(), 7);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 1);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(2).getHealth(), 7);

		assertEquals(board.data_.getMinion_p0(0).getAttack(), 5);
		assertEquals(board.data_.getMinion_p0(1).getAttack(), 2);
		assertEquals(board.data_.getMinion_p0(2).getAttack(), 7);
		assertEquals(board.data_.getMinion_p1(0).getAttack(), 4);
		assertEquals(board.data_.getMinion_p1(1).getAttack(), 2);
		assertEquals(board.data_.getMinion_p1(2).getAttack(), 7);
		
		assertTrue(board.data_.getMinion_p1(0).getDivineShield());
	}

	@Test
	public void test2() throws HSInvalidPlayerIndexException {
		
		//null case
		Minion target = board.data_.getCharacter(0, 0);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(0, target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getNumMinions_p0(), 3);
		assertEquals(board.data_.getNumMinions_p1(), 3);
		assertEquals(board.data_.getMana_p0(), 3);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 4);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(2).getHealth(), 7);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 1);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(2).getHealth(), 7);

		assertEquals(board.data_.getMinion_p0(0).getAttack(), 5);
		assertEquals(board.data_.getMinion_p0(1).getAttack(), 2);
		assertEquals(board.data_.getMinion_p0(2).getAttack(), 7);
		assertEquals(board.data_.getMinion_p1(0).getAttack(), 4);
		assertEquals(board.data_.getMinion_p1(1).getAttack(), 2);
		assertEquals(board.data_.getMinion_p1(2).getAttack(), 7);
		
		assertTrue(board.data_.getMinion_p1(0).getDivineShield());
		
		//attack the Ogre... should kill everything except the Scarlet Crusader
		target = board.data_.getCharacter(1, 3);
		Minion attacker = board.data_.getCharacter(0, 1);
		attacker.hasAttacked(false);
		ret = attacker.attack(1, target, ret, null, null);

		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getNumMinions_p0(), 1);
		assertEquals(board.data_.getNumMinions_p1(), 1);
		assertEquals(board.data_.getMana_p0(), 3);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getHero_p0().getHealth(), 28);
		assertEquals(board.data_.getHero_p1().getHealth(), 28);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 5);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 1);

		assertEquals(board.data_.getMinion_p0(0).getAttack(), 6);
		assertEquals(board.data_.getMinion_p1(0).getAttack(), 3);
		
		assertFalse(board.data_.getMinion_p1(0).getDivineShield());

	}
	
	@Test
	public void test3() throws HSInvalidPlayerIndexException {
		
		//null case
		Minion target = board.data_.getCharacter(0, 0);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(0, target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getNumMinions_p0(), 3);
		assertEquals(board.data_.getNumMinions_p1(), 3);
		assertEquals(board.data_.getMana_p0(), 3);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 4);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(2).getHealth(), 7);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 1);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(2).getHealth(), 7);

		assertEquals(board.data_.getMinion_p0(0).getAttack(), 5);
		assertEquals(board.data_.getMinion_p0(1).getAttack(), 2);
		assertEquals(board.data_.getMinion_p0(2).getAttack(), 7);
		assertEquals(board.data_.getMinion_p1(0).getAttack(), 4);
		assertEquals(board.data_.getMinion_p1(1).getAttack(), 2);
		assertEquals(board.data_.getMinion_p1(2).getAttack(), 7);
		
		assertTrue(board.data_.getMinion_p1(0).getDivineShield());
		
		//Silence the Abomination first, then attack with it
		target = board.data_.getCharacter(1, 3);
		Minion attacker = board.data_.getCharacter(0, 1);
		attacker.silenced(0, board, null, null);
		attacker.hasAttacked(false);
		ret = attacker.attack(1, target, ret, null, null);

		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getNumMinions_p0(), 2);
		assertEquals(board.data_.getNumMinions_p1(), 3);
		assertEquals(board.data_.getMana_p0(), 3);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 7);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 1);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(2).getHealth(), 2);

		assertEquals(board.data_.getMinion_p0(0).getAttack(), 2);
		assertEquals(board.data_.getMinion_p0(1).getAttack(), 7);
		assertEquals(board.data_.getMinion_p1(0).getAttack(), 4);
		assertEquals(board.data_.getMinion_p1(1).getAttack(), 2);
		assertEquals(board.data_.getMinion_p1(2).getAttack(), 7);
		
		assertTrue(board.data_.getMinion_p1(0).getDivineShield());

	}
}
