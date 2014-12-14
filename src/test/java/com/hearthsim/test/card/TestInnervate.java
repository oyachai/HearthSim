package com.hearthsim.test.card;

import java.util.List;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.concrete.Innervate;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.util.HearthActionBoardPair;
import com.hearthsim.util.tree.HearthTreeNode;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestInnervate {

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
		
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_1);
		
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_1);
		
		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}
	
		deck = new Deck(cards);

		Innervate fb = new Innervate();
		board.data_.placeCardHandCurrentPlayer(fb);

		board.data_.getCurrentPlayer().setMana((byte)4);
		board.data_.getWaitingPlayer().setMana((byte)4);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)4);
		board.data_.getWaitingPlayer().setMaxMana((byte)4);
		
	}
	
	@Test
	public void test2() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 6);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 4);
		assertEquals(board.data_.getCurrentPlayer().getMaxMana(), 4);
		assertEquals(board.data_.getWaitingPlayer().getMaxMana(), 4);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1 - 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1 - 1);
	}
	
	@Test
	public void test3() throws HSException {
		
		board.data_.getCurrentPlayer().setMana((byte)10);
		board.data_.getCurrentPlayer().setMaxMana((byte)10);
		
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, deck, null);		
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 10);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 4);
		assertEquals(board.data_.getCurrentPlayer().getMaxMana(), 10);
		assertEquals(board.data_.getWaitingPlayer().getMaxMana(), 4);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1 - 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1 - 1);
	}

	@Test
	public void testAiDoesNotPlayWithoutReason() throws HSException {
		
		board.data_.getCurrentPlayer().setMana((byte)3);
		board.data_.getWaitingPlayer().setMana((byte)3);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)3);
		board.data_.getWaitingPlayer().setMaxMana((byte)3);

		board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 1).hasAttacked(true);
		board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 2).hasAttacked(true);

        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
        List<HearthActionBoardPair> ab = ai0.playTurn(0, board.data_);
		BoardModel resBoard = ab.get(ab.size() - 1).board;
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getNumMinions(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(resBoard).getNumMinions(), 2);
		assertEquals(resBoard.getCurrentPlayer().getMana(), 3);
		assertEquals(resBoard.getWaitingPlayer().getMana(), 3);
		assertEquals(resBoard.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(resBoard.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(resBoard.getNumCardsHandCurrentPlayer(), 1);
		assertEquals(resBoard.getNumCardsHandWaitingPlayer(), 0);
	}
}
