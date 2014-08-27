package com.hearthsim.test.minion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.AcolyteOfPain;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.GoldshireFootman;
import com.hearthsim.exception.HSException;
import com.hearthsim.player.Player;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.util.boardstate.BoardState;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestAcolyteOfPain {
	
	private HearthTreeNode board;
	private Deck deck;


	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardState());

		Minion minion0_0 = new AcolyteOfPain();
		Minion minion0_1 = new GoldshireFootman();
		Minion minion1_0 = new GoldshireFootman();
		Minion minion1_1 = new GoldshireFootman();
		
		board.data_.placeMinion(0, minion0_0);
		board.data_.placeMinion(0, minion0_1);
		
		board.data_.placeMinion(1, minion1_0);
		board.data_.placeMinion(1, minion1_1);
		
		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new BloodfenRaptor();
		}
	
		deck = new Deck(cards);

		Minion fb = new AcolyteOfPain();
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
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 3);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), 2);
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
		assertEquals(board.data_.getMana_p0(), 4);
		assertEquals(board.data_.getMana_p1(), 7);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 3);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(2).getHealth(), 3);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(0).getTotalAttack(), 1);
		assertEquals(board.data_.getMinion_p0(1).getTotalAttack(), 1);
		assertEquals(board.data_.getMinion_p0(2).getTotalAttack(), 1);
		assertEquals(board.data_.getMinion_p1(0).getTotalAttack(), 1);
		assertEquals(board.data_.getMinion_p1(1).getTotalAttack(), 1);
	}
	
	@Test
	public void test3() throws HSException {
		
		
		ArtificialPlayer ai0 = new ArtificialPlayer(
				0.9,
				0.9,
				1.0,
				1.0,
				1.0,
				0.1,
				0.1,
				0.1,
				0.5,
				0.5,
				0.0,
				0.5,
				0.0,
				0.0
				);
		
		Hero hero = new Hero();
		Player player0 = new Player("player0", hero, deck);
		Player player1 = new Player("player0", hero, deck);
		
		board.data_.setMana_p0((byte)1);
		board.data_.setMana_p1((byte)1);
		
		board.data_.setMaxMana_p0((byte)1);
		board.data_.setMaxMana_p1((byte)1);

		
		BoardState resBoard = ai0.playTurn(0, board.data_, player0, player1);
		
		assertEquals(resBoard.getNumCards_hand_p0(), 2); //1 card drawn from AcolyteOfPain, not enough mana to play it
		assertEquals(resBoard.getNumMinions_p0(), 2);
		assertEquals(resBoard.getNumMinions_p1(), 1); //1 minion should have been killed
		assertEquals(resBoard.getMana_p0(), 1); //0 mana used
		assertEquals(resBoard.getMana_p1(), 1);
		assertEquals(resBoard.getHero_p0().getHealth(), 30);
		assertEquals(resBoard.getHero_p1().getHealth(), 30);
	}
	
	@Test
	public void test4() throws HSException {
		
		
		ArtificialPlayer ai0 = new ArtificialPlayer(
				0.9,
				0.9,
				1.0,
				1.0,
				1.0,
				0.1,
				0.1,
				0.1,
				0.5,
				0.5,
				0.0,
				0.5,
				0.0,
				0.0
				);
		
		Hero hero = new Hero();
		Player player0 = new Player("player0", hero, deck);
		Player player1 = new Player("player0", hero, deck);
		
		board.data_.setMana_p0((byte)3);
		board.data_.setMana_p1((byte)3);
		
		board.data_.setMaxMana_p0((byte)3);
		board.data_.setMaxMana_p1((byte)3);


		BoardState resBoard = ai0.playTurn(0, board.data_, player0, player1, 200000000);
		
		assertEquals(resBoard.getNumCards_hand_p0(), 1); //1 card drawn from AcolyteOfPain, then played the Bloodfen Raptor
		assertEquals(resBoard.getNumMinions_p0(), 3);
		assertEquals(resBoard.getNumMinions_p1(), 1); //1 minion should have been killed
		assertEquals(resBoard.getMana_p0(), 1); //2 mana used... it's better to put down a Bloodfen Raptor than an Acolyte of Pain
		assertEquals(resBoard.getMana_p1(), 3);
		assertEquals(resBoard.getHero_p0().getHealth(), 30);
		assertEquals(resBoard.getHero_p1().getHealth(), 30);
	}
	
	@Test
	public void test5() throws HSException {
		
		
		ArtificialPlayer ai0 = new ArtificialPlayer(
				0.9,
				0.9,
				1.0,
				1.0,
				1.0,
				0.1,
				0.1,
				0.1,
				0.5,
				0.5,
				0.0,
				0.5,
				0.0,
				0.0
				);
		
		Hero hero = new Hero();
		Player player0 = new Player("player0", hero, deck);
		Player player1 = new Player("player0", hero, deck);
		
		board.data_.setMana_p0((byte)3);
		board.data_.setMana_p1((byte)3);
		
		board.data_.setMaxMana_p0((byte)3);
		board.data_.setMaxMana_p1((byte)3);

		board.data_.removeMinion(1, 0);
		board.data_.removeMinion(1, 0);
		board.data_.placeMinion(1, new AcolyteOfPain());
		
		assertEquals(board.data_.getNumCards_hand_p1(), 0);

		BoardState resBoard = ai0.playTurn(0, board.data_, player0, player1);
		
		assertEquals(resBoard.getNumCards_hand_p0(), 1); //1 card drawn from AcolyteOfPain, then played the Bloodfen Raptor
		assertEquals(resBoard.getNumCards_hand_p1(), 1); //1 card drawn from AcolyteOfPain.  The Acolytes smack into each other.
		assertEquals(resBoard.getNumMinions_p0(), 3);
		assertEquals(resBoard.getNumMinions_p1(), 1); //1 minion should have been killed
		assertEquals(resBoard.getMana_p0(), 1); //2 mana used... it's better to put down a Bloodfen Raptor than an Acolyte of Pain
		assertEquals(resBoard.getMana_p1(), 3);
		assertEquals(resBoard.getHero_p0().getHealth(), 30);
		assertEquals(resBoard.getHero_p1().getHealth(), 29);
	}
}
