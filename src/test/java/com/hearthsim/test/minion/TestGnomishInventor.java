package com.hearthsim.test.minion;

import java.util.List;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.GnomishInventor;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.util.HearthActionBoardPair;
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
		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new BloodfenRaptor();
		}
	
		deck = new Deck(cards);
		PlayerModel playerModel0 = new PlayerModel((byte)0, "player0", new TestHero(), deck);
		PlayerModel playerModel1 = new PlayerModel((byte)1, "player1", new TestHero(), deck);

		board = new HearthTreeNode(new BoardModel(playerModel0, playerModel1));

		Minion minion0_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion0_1 = new Minion("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);
		Minion minion1_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion1_1 = new Minion("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);
		
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_1);
		
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_1);
		
		deck = new Deck(cards);

		Minion fb = new GnomishInventor();
		board.data_.placeCardHandCurrentPlayer(fb);

		board.data_.getCurrentPlayer().setMana((byte)7);
		board.data_.getWaitingPlayer().setMana((byte)7);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)7);
		board.data_.getWaitingPlayer().setMaxMana((byte)7);
		
	}
	
	@Test
	public void test0() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 0, board, deck, null);
		
		assertNull(ret);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1 - 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1 - 1);
	}
	
	@Test
	public void test2() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board, deck, null);
		
		assertFalse(ret == null);
		assertTrue( ret instanceof CardDrawNode );
		
		CardDrawNode cNode = (CardDrawNode)ret;
		
		assertEquals(cNode.getNumCardsToDraw(), 1);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 3);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 7);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 4);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getHealth(), health1 - 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1 - 1);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), attack0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalAttack(), attack0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), attack0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), attack0);
	}
	
	@Test
	public void test3() throws HSException {

		board.data_.getCurrentPlayer().setMana((byte)5);
		board.data_.getWaitingPlayer().setMana((byte)5);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)5);
		board.data_.getWaitingPlayer().setMaxMana((byte)5);


        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
        List<HearthActionBoardPair> ab = ai0.playTurn(0, board.data_);
		BoardModel resBoard = ab.get(ab.size() - 1).board;
		
		assertEquals(resBoard.getNumCardsHandCurrentPlayer(), 1); //1 card drawn from GnomishInventor, not enough mana to play it
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getNumMinions(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(resBoard).getNumMinions(), 1); //1 minion should have been killed
		assertEquals(resBoard.getCurrentPlayer().getMana(), 1); //4 mana used for Gnomish Inventor
		assertEquals(resBoard.getWaitingPlayer().getMana(), 5);
		assertEquals(resBoard.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(resBoard.getWaitingPlayerHero().getHealth(), 25); //smacked in the face once
	}
	
	@Test
	public void test4() throws HSException {

        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
        List<HearthActionBoardPair> ab = ai0.playTurn(0, board.data_);
		BoardModel resBoard = ab.get(ab.size() - 1).board;
		
		assertEquals(resBoard.getNumCardsHandCurrentPlayer(), 0); //1 card drawn from GnomishInventor, and had enough mana to play it
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getNumMinions(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(resBoard).getNumMinions(), 1); //1 minion should have been killed
		assertEquals(resBoard.getCurrentPlayer().getMana(), 1); //4 mana used for Gnomish Inventor, 2 for Bloodfen Raptor
		assertEquals(resBoard.getWaitingPlayer().getMana(), 7);
		assertEquals(resBoard.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(resBoard.getWaitingPlayerHero().getHealth(), 25); //smacked in the face once
	}
}
