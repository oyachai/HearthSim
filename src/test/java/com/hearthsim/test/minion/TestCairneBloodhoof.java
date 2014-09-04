package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.*;
import com.hearthsim.card.spellcard.concrete.Fireball;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.boardstate.BoardState;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestCairneBloodhoof {


	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() {
		board = new HearthTreeNode(new BoardState());

		Minion minion0_0 = new BoulderfistOgre();
		Minion minion0_1 = new RaidLeader();
		Minion minion1_0 = new BoulderfistOgre();
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

		Card fb = new CairneBloodhoof();
		board.data_.placeCard_hand_p0(fb);

		board.data_.setMana_p0((byte)8);
		board.data_.setMana_p1((byte)8);
		
		board.data_.setMaxMana_p0((byte)8);
		board.data_.setMaxMana_p1((byte)8);
		
		HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
		try {
			tmpBoard.data_.getCard_hand_p0(0).useOn(0, tmpBoard.data_.getHero_p0(), tmpBoard, deck, null);
			tmpBoard.data_.getCard_hand_p0(0).useOn(0, tmpBoard.data_.getHero_p0(), tmpBoard, deck, null);
		} catch (HSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
		try {
			board.data_.getCard_hand_p0(0).useOn(0, board.data_.getHero_p0(), board, deck, null);
			board.data_.getCard_hand_p0(0).useOn(0, board.data_.getHero_p0(), board, deck, null);
		} catch (HSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board.data_.resetMana();
		board.data_.resetMinions();
		
	}
	
	

	@Test
	public void test0() throws HSException {
		
		//null case
		Minion target = board.data_.getCharacter(1, 0);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(1, target, board, deck, null);
		
		assertTrue(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getNumMinions_p0(), 2);
		assertEquals(board.data_.getNumMinions_p1(), 2);
		assertEquals(board.data_.getMana_p0(), 8);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 7);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), 7);

		assertEquals(board.data_.getMinion_p0(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p0(1).getTotalAttack(), 7);
		assertEquals(board.data_.getMinion_p1(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p1(1).getTotalAttack(), 7);
	}
	
	@Test
	public void test2() throws HSException {
		
		//null case
		Minion target = board.data_.getCharacter(0, 2);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(0, target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getNumMinions_p0(), 3);
		assertEquals(board.data_.getNumMinions_p1(), 2);
		assertEquals(board.data_.getMana_p0(), 2);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 7);
		assertEquals(board.data_.getMinion_p0(2).getHealth(), 5);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), 7);

		assertEquals(board.data_.getMinion_p0(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p0(1).getTotalAttack(), 7);
		assertEquals(board.data_.getMinion_p0(2).getTotalAttack(), 5);
		assertEquals(board.data_.getMinion_p1(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p1(1).getTotalAttack(), 7);
		
		
		
		//----------------------------------------------------------
		
		Minion minion = board.data_.getMinion_p0(2);
		assertTrue(minion instanceof CairneBloodhoof);
		
		minion.hasAttacked(false);
		target = board.data_.getCharacter(1, 2);
		minion.attack(1, target, board, deck, null);

		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getNumMinions_p0(), 3);
		assertEquals(board.data_.getNumMinions_p1(), 2);
		assertEquals(board.data_.getMana_p0(), 2);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 7);
		assertEquals(board.data_.getMinion_p0(2).getHealth(), 5);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), 7 - 5);

		assertEquals(board.data_.getMinion_p0(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p0(1).getTotalAttack(), 7);
		assertEquals(board.data_.getMinion_p0(2).getTotalAttack(), 5);
		assertEquals(board.data_.getMinion_p1(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p1(1).getTotalAttack(), 7);
		
		assertTrue(board.data_.getMinion_p0(2) instanceof BaineBloodhoof);
		
	}

	
	@Test
	public void test3() throws HSException {
		
		//null case
		Minion target = board.data_.getCharacter(0, 1);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(0, target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getNumMinions_p0(), 3);
		assertEquals(board.data_.getNumMinions_p1(), 2);
		assertEquals(board.data_.getMana_p0(), 2);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 5);
		assertEquals(board.data_.getMinion_p0(2).getHealth(), 7);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), 7);

		assertEquals(board.data_.getMinion_p0(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p0(1).getTotalAttack(), 5);
		assertEquals(board.data_.getMinion_p0(2).getTotalAttack(), 7);
		assertEquals(board.data_.getMinion_p1(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p1(1).getTotalAttack(), 7);
		
		
		
		//----------------------------------------------------------
		
		Minion minion = board.data_.getMinion_p0(1);
		assertTrue(minion instanceof CairneBloodhoof);
		
		minion.hasAttacked(false);
		target = board.data_.getCharacter(1, 2);
		minion.attack(1, target, board, deck, null);

		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getNumMinions_p0(), 3);
		assertEquals(board.data_.getNumMinions_p1(), 2);
		assertEquals(board.data_.getMana_p0(), 2);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 5);
		assertEquals(board.data_.getMinion_p0(2).getHealth(), 7);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), 7 - 5);

		assertEquals(board.data_.getMinion_p0(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p0(1).getTotalAttack(), 5);
		assertEquals(board.data_.getMinion_p0(2).getTotalAttack(), 7);
		assertEquals(board.data_.getMinion_p1(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p1(1).getTotalAttack(), 7);
		
		assertTrue(board.data_.getMinion_p0(1) instanceof BaineBloodhoof);
		
	}

	@Test
	public void test4() throws HSException {
		
		//null case
		Minion target = board.data_.getCharacter(0, 1);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(0, target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getNumMinions_p0(), 3);
		assertEquals(board.data_.getNumMinions_p1(), 2);
		assertEquals(board.data_.getMana_p0(), 2);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 5);
		assertEquals(board.data_.getMinion_p0(2).getHealth(), 7);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), 7);

		assertEquals(board.data_.getMinion_p0(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p0(1).getTotalAttack(), 5);
		assertEquals(board.data_.getMinion_p0(2).getTotalAttack(), 7);
		assertEquals(board.data_.getMinion_p1(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p1(1).getTotalAttack(), 7);
		
		
		//----------------------------------------------------------
		HearthTreeNode fb = new HearthTreeNode(board.data_.flipPlayers());
		
		Minion minion = fb.data_.getMinion_p0(1);
		
		minion.hasAttacked(false);
		target = fb.data_.getCharacter(1, 2);
		minion.attack(1, target, fb, deck, null);

		assertEquals(fb.data_.getNumCards_hand(), 0);
		assertEquals(fb.data_.getNumMinions_p0(), 2);
		assertEquals(fb.data_.getNumMinions_p1(), 3);
		assertEquals(fb.data_.getMana_p0(), 8);
		assertEquals(fb.data_.getMana_p1(), 2);
		assertEquals(fb.data_.getHero_p0().getHealth(), 30);
		assertEquals(fb.data_.getHero_p1().getHealth(), 30);
		assertEquals(fb.data_.getMinion_p0(0).getHealth(), 2);
		assertEquals(fb.data_.getMinion_p0(1).getHealth(), 7 - 5);
		assertEquals(fb.data_.getMinion_p1(0).getHealth(), 2);
		assertEquals(fb.data_.getMinion_p1(1).getHealth(), 5);
		assertEquals(fb.data_.getMinion_p1(2).getHealth(), 7);

		assertEquals(fb.data_.getMinion_p0(0).getTotalAttack(), 2);
		assertEquals(fb.data_.getMinion_p0(1).getTotalAttack(), 7);
		assertEquals(fb.data_.getMinion_p1(0).getTotalAttack(), 2);
		assertEquals(fb.data_.getMinion_p1(1).getTotalAttack(), 5);
		assertEquals(fb.data_.getMinion_p1(2).getTotalAttack(), 7);
		
		assertTrue(fb.data_.getMinion_p1(1) instanceof BaineBloodhoof);
		
	}
	
	@Test
	public void test5() throws HSException {
		
		//null case
		Minion target = board.data_.getCharacter(0, 1);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(0, target, board, deck, null);

		ret = new BloodfenRaptor().summonMinion(0, board.data_.getHero_p0(), ret, null, null, false);
		ret = new BloodfenRaptor().summonMinion(0, board.data_.getHero_p0(), ret, null, null, false);
		ret = new BloodfenRaptor().summonMinion(0, board.data_.getHero_p0(), ret, null, null, false);
		ret = new BloodfenRaptor().summonMinion(0, board.data_.getHero_p0(), ret, null, null, false);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getNumMinions_p0(), 7);
		assertEquals(board.data_.getNumMinions_p1(), 2);
		assertEquals(board.data_.getMana_p0(), 2);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(2).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(3).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(4).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(5).getHealth(), 5);
		assertEquals(board.data_.getMinion_p0(6).getHealth(), 7);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), 7);

		assertEquals(board.data_.getMinion_p0(0).getTotalAttack(), 4);
		assertEquals(board.data_.getMinion_p0(1).getTotalAttack(), 4);
		assertEquals(board.data_.getMinion_p0(2).getTotalAttack(), 4);
		assertEquals(board.data_.getMinion_p0(3).getTotalAttack(), 4);
		assertEquals(board.data_.getMinion_p0(4).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p0(5).getTotalAttack(), 5);
		assertEquals(board.data_.getMinion_p0(6).getTotalAttack(), 7);
		assertEquals(board.data_.getMinion_p1(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p1(1).getTotalAttack(), 7);
		
		
		//----------------------------------------------------------
		HearthTreeNode fb = new HearthTreeNode(board.data_.flipPlayers());
		
		Minion minion = fb.data_.getMinion_p0(1);
		
		minion.hasAttacked(false);
		target = fb.data_.getCharacter(1, 6);
		minion.attack(1, target, fb, deck, null);

		assertEquals(fb.data_.getNumCards_hand(), 0);
		assertEquals(fb.data_.getNumMinions_p0(), 2);
		assertEquals(fb.data_.getNumMinions_p1(), 7);
		assertEquals(fb.data_.getMana_p0(), 8);
		assertEquals(fb.data_.getMana_p1(), 2);
		assertEquals(fb.data_.getHero_p0().getHealth(), 30);
		assertEquals(fb.data_.getHero_p1().getHealth(), 30);
		assertEquals(fb.data_.getMinion_p0(0).getHealth(), 2);
		assertEquals(fb.data_.getMinion_p0(1).getHealth(), 7 - 5);
		assertEquals(fb.data_.getMinion_p1(0).getHealth(), 2);
		assertEquals(fb.data_.getMinion_p1(1).getHealth(), 2);
		assertEquals(fb.data_.getMinion_p1(2).getHealth(), 2);
		assertEquals(fb.data_.getMinion_p1(3).getHealth(), 2);
		assertEquals(fb.data_.getMinion_p1(4).getHealth(), 2);
		assertEquals(fb.data_.getMinion_p1(5).getHealth(), 5);
		assertEquals(fb.data_.getMinion_p1(6).getHealth(), 7);

		assertEquals(fb.data_.getMinion_p0(0).getTotalAttack(), 2);
		assertEquals(fb.data_.getMinion_p0(1).getTotalAttack(), 7);
		assertEquals(fb.data_.getMinion_p1(0).getTotalAttack(), 4);
		assertEquals(fb.data_.getMinion_p1(1).getTotalAttack(), 4);
		assertEquals(fb.data_.getMinion_p1(2).getTotalAttack(), 4);
		assertEquals(fb.data_.getMinion_p1(3).getTotalAttack(), 4);
		assertEquals(fb.data_.getMinion_p1(4).getTotalAttack(), 2);
		assertEquals(fb.data_.getMinion_p1(5).getTotalAttack(), 5);
		assertEquals(fb.data_.getMinion_p1(6).getTotalAttack(), 7);
		
		assertTrue(fb.data_.getMinion_p1(5) instanceof BaineBloodhoof);
		
	}
	
	
	@Test
	public void test6() throws HSException {
		
		//In this test Cairne will be killed by a spell: Fireball
		
		//null case
		Minion target = board.data_.getCharacter(0, 1);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(0, target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getNumMinions_p0(), 3);
		assertEquals(board.data_.getNumMinions_p1(), 2);
		assertEquals(board.data_.getMana_p0(), 2);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 5);
		assertEquals(board.data_.getMinion_p0(2).getHealth(), 7);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), 7);

		assertEquals(board.data_.getMinion_p0(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p0(1).getTotalAttack(), 5);
		assertEquals(board.data_.getMinion_p0(2).getTotalAttack(), 7);
		assertEquals(board.data_.getMinion_p1(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p1(1).getTotalAttack(), 7);
		
		
		//----------------------------------------------------------
		HearthTreeNode fb = new HearthTreeNode(board.data_.flipPlayers());
		
		Minion minion = fb.data_.getMinion_p0(1);
		
		fb.data_.placeCard_hand_p0(new Fireball());
		Card fireball = fb.data_.getCard_hand_p0(0);
		
		minion.hasAttacked(false);
		target = fb.data_.getCharacter(1, 2);
		fireball.useOn(1, target, fb, deck, null);

		assertEquals(fb.data_.getNumCards_hand(), 0);
		assertEquals(fb.data_.getNumMinions_p0(), 2);
		assertEquals(fb.data_.getNumMinions_p1(), 3);
		assertEquals(fb.data_.getMana_p0(), 4);
		assertEquals(fb.data_.getMana_p1(), 2);
		assertEquals(fb.data_.getHero_p0().getHealth(), 30);
		assertEquals(fb.data_.getHero_p1().getHealth(), 30);
		assertEquals(fb.data_.getMinion_p0(0).getHealth(), 2);
		assertEquals(fb.data_.getMinion_p0(1).getHealth(), 7);
		assertEquals(fb.data_.getMinion_p1(0).getHealth(), 2);
		assertEquals(fb.data_.getMinion_p1(1).getHealth(), 5);
		assertEquals(fb.data_.getMinion_p1(2).getHealth(), 7);

		assertEquals(fb.data_.getMinion_p0(0).getTotalAttack(), 2);
		assertEquals(fb.data_.getMinion_p0(1).getTotalAttack(), 7);
		assertEquals(fb.data_.getMinion_p1(0).getTotalAttack(), 2);
		assertEquals(fb.data_.getMinion_p1(1).getTotalAttack(), 5);
		assertEquals(fb.data_.getMinion_p1(2).getTotalAttack(), 7);
		
		assertTrue(fb.data_.getMinion_p1(1) instanceof BaineBloodhoof);
		
	}
}
