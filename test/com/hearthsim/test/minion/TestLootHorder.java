package com.hearthsim.test.minion;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.LootHoarder;
import com.hearthsim.card.minion.concrete.RiverCrocolisk;
import com.hearthsim.card.minion.concrete.Wisp;
import com.hearthsim.exception.HSException;
import com.hearthsim.player.Player;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestLootHorder {
	
	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardState());

		Minion minion0_0 = new LootHoarder();
		Minion minion1_0 = new BloodfenRaptor();
		
		board.data_.placeMinion(0, minion0_0);
		
		board.data_.placeMinion(1, minion1_0);
		
		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new RiverCrocolisk();
		}
	
		deck = new Deck(cards);
		
	}
	
	
	@Test
	public void test0() throws HSException {
		
		
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
		
		assertEquals(resBoard.getNumCards_hand_p0(), 1); //1 card drawn from Loot Horder attacking and dying, no mana left to play the card
		assertEquals(resBoard.getNumMinions_p0(), 0);
		assertEquals(resBoard.getNumMinions_p1(), 0); //1 minion should have been killed
		assertEquals(resBoard.getMana_p0(), 1); //no mana used
		assertEquals(resBoard.getMana_p1(), 1);
		assertEquals(resBoard.getHero_p0().getHealth(), 30);
		assertEquals(resBoard.getHero_p1().getHealth(), 30);
	}
	

	@Test
	public void test1() throws HSException {
		
		
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


		BoardState resBoard = ai0.playTurn(0, board.data_, player0, player1);
		
		assertEquals(resBoard.getNumCards_hand_p0(), 0); //1 card drawn from Loot Horder attacking and dying, then played the drawn card
		assertEquals(resBoard.getNumMinions_p0(), 1);
		assertEquals(resBoard.getNumMinions_p1(), 0); //1 minion should have been killed
		assertEquals(resBoard.getMana_p0(), 1); //2 mana used
		assertEquals(resBoard.getMana_p1(), 3);
		assertEquals(resBoard.getHero_p0().getHealth(), 30);
		assertEquals(resBoard.getHero_p1().getHealth(), 30);
	}
	
	@Test
	public void test2() throws HSException {
				
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
		
		//remove the loot hoarder from player0, add a Wisp
		board.data_.removeMinion(0, 0);
		board.data_.placeMinion(0, new Wisp());
		
		//remove the Bloodfen Raptor from player0, add a Loot Hoarder
		board.data_.removeMinion(1, 0);
		board.data_.placeMinion(1, new LootHoarder());

		Hero hero = new Hero();
		Player player0 = new Player("player0", hero, deck);
		Player player1 = new Player("player0", hero, deck);
		
		board.data_.setMana_p0((byte)3);
		board.data_.setMana_p1((byte)3);
		
		board.data_.setMaxMana_p0((byte)3);
		board.data_.setMaxMana_p1((byte)3);


		BoardState resBoard = ai0.playTurn(0, board.data_, player0, player1);
		
		assertEquals(resBoard.getNumCards_hand_p0(), 0); //no cards in hand
		assertEquals(resBoard.getNumCards_hand_p1(), 1); //drew cards from the loot horder that was killed
		assertEquals(resBoard.getNumMinions_p0(), 0);
		assertEquals(resBoard.getNumMinions_p1(), 0); //1 minion should have been killed
		assertEquals(resBoard.getMana_p0(), 3); //no mana used
		assertEquals(resBoard.getMana_p1(), 3);
		assertEquals(resBoard.getHero_p0().getHealth(), 30);
		assertEquals(resBoard.getHero_p1().getHealth(), 30);
	}
}
