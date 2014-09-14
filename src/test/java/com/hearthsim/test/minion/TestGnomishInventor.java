package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.GnomishInventor;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestGnomishInventor {

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
			cards[index] = new BloodfenRaptor();
		}
	
		deck = new Deck(cards);

		Minion fb = new GnomishInventor();
		board.data_.placeCardHandCurrentPlayer(fb);

		board.data_.setMana_p0((byte)7);
		board.data_.setMana_p1((byte)7);
		
		board.data_.setMaxMana_p0((byte)7);
		board.data_.setMaxMana_p1((byte)7);
		
	}
	
	@Test
	public void test0() throws HSException {
		
		Minion target = board.data_.getCharacter(board.data_.getWaitingPlayer(), 0);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
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
	public void test2() throws HSException {
		
		Minion target = board.data_.getCharacter(board.data_.getCurrentPlayer(), 1);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(board.data_.getCurrentPlayer(), target, board, deck, null);
		
		assertFalse(ret == null);
		assertTrue( ret instanceof CardDrawNode );
		
		CardDrawNode cNode = (CardDrawNode)ret;
		
		assertEquals(cNode.getNumCardsToDraw(), 1);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getCurrentPlayer().getNumMinions(), 3);
		assertEquals(board.data_.getWaitingPlayer().getNumMinions(), 2);
		assertEquals(board.data_.getMana_p0(), 3);
		assertEquals(board.data_.getMana_p1(), 7);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getHealth(), health0);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getHealth(), 4);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(2).getHealth(), health1 - 1);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getHealth(), health0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getHealth(), health1 - 1);

		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), attack0);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), attack0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), attack0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), attack0);
	}
	
	@Test
	public void test3() throws HSException {
		

		
		Hero hero = new Hero();
		PlayerModel playerModel0 = new PlayerModel("player0", hero, deck);
		PlayerModel playerModel1 = new PlayerModel("player0", hero, deck);
		
		board.data_.setMana_p0((byte)5);
		board.data_.setMana_p1((byte)5);
		
		board.data_.setMaxMana_p0((byte)5);
		board.data_.setMaxMana_p1((byte)5);


        ArtificialPlayer ai0 = ArtificialPlayer.buildStandardAI1();
		BoardModel resBoard = ai0.playTurn(0, board.data_, playerModel0, playerModel1);
		
		assertEquals(resBoard.getNumCardsHandCurrentPlayer(), 1); //1 card drawn from GnomishInventor, not enough mana to play it
		assertEquals(resBoard.getCurrentPlayer().getNumMinions(), 3);
		assertEquals(resBoard.getWaitingPlayer().getNumMinions(), 1); //1 minion should have been killed
		assertEquals(resBoard.getMana_p0(), 1); //4 mana used for Gnomish Inventor
		assertEquals(resBoard.getMana_p1(), 5);
		assertEquals(resBoard.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(resBoard.getWaitingPlayerHero().getHealth(), 25); //smacked in the face once
	}
	
	@Test
	public void test4() throws HSException {
		

		Hero hero = new Hero();		
		PlayerModel playerModel0 = new PlayerModel("player0", hero, deck);
		PlayerModel playerModel1 = new PlayerModel("player0", hero, deck);

        ArtificialPlayer ai0 = ArtificialPlayer.buildStandardAI1();
		BoardModel resBoard = ai0.playTurn(0, board.data_, playerModel0, playerModel1);
		
		assertEquals(resBoard.getNumCardsHandCurrentPlayer(), 0); //1 card drawn from GnomishInventor, and had enough mana to play it
		assertEquals(resBoard.getCurrentPlayer().getNumMinions(), 4);
		assertEquals(resBoard.getWaitingPlayer().getNumMinions(), 1); //1 minion should have been killed
		assertEquals(resBoard.getMana_p0(), 1); //4 mana used for Gnomish Inventor, 2 for Bloodfen Raptor
		assertEquals(resBoard.getMana_p1(), 7);
		assertEquals(resBoard.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(resBoard.getWaitingPlayerHero().getHealth(), 25); //smacked in the face once
	}
}
