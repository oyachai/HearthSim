package com.hearthsim.test.card;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.concrete.HolySmite;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestHolySmite {

	private HearthTreeNode board;
	private static final byte mana = 2;
	private static final byte attack0 = 2;
	private static final byte health0 = 2;
	private static final byte health1 = 3;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel());

		Minion minion0_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion0_1 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1);
		Minion minion1_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion1_1 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1);
		
		board.data_.placeMinion(board.data_.getCurrentPlayer(), minion0_0);
		board.data_.placeMinion(board.data_.getCurrentPlayer(), minion0_1);
		
		board.data_.placeMinion(board.data_.getWaitingPlayer(), minion1_0);
		board.data_.placeMinion(board.data_.getWaitingPlayer(), minion1_1);
				
	}
	
	@Test
	public void test0() throws HSException {
		HolySmite hs = new HolySmite();
		board.data_.placeCardHandCurrentPlayer(hs);
		
		Minion target = board.data_.getCharacter(board.data_.getCurrentPlayer(), 0);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		theCard.useOn(board.data_.getCurrentPlayer(), target, board, null, null);
		
		assertTrue("test0_1", board.data_.getNumCards_hand() == 0);
		assertTrue("test0_2", board.data_.getCurrentPlayer().getNumMinions() == 2);
		assertTrue("test0_3", board.data_.getWaitingPlayer().getNumMinions() == 2);
		assertTrue("test0_4", board.data_.getCurrentPlayerHero().getHealth() == 28);
		assertTrue("test0_5", board.data_.getWaitingPlayerHero().getHealth() == 30);
		assertTrue("test0_6", board.data_.getCurrentPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue("test0_7", board.data_.getCurrentPlayer().getMinions().get(1).getHealth() == health1);
		assertTrue("test0_8", board.data_.getWaitingPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue("test0_9", board.data_.getWaitingPlayer().getMinions().get(1).getHealth() == health1);
	}

	@Test
	public void test1() throws HSException {
		HolySmite hs = new HolySmite();
		board.data_.placeCardHandCurrentPlayer(hs);
		
		Minion target = board.data_.getCharacter(board.data_.getWaitingPlayer(), 0);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		theCard.useOn(board.data_.getWaitingPlayer(), target, board, null, null);
		
		assertTrue("test1_1", board.data_.getNumCards_hand() == 0);
		assertTrue("test1_2", board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue("test1_3", board.data_.getWaitingPlayerHero().getHealth() == 28);
		assertTrue("test1_4", board.data_.getCurrentPlayer().getNumMinions() == 2);
		assertTrue("test1_5", board.data_.getWaitingPlayer().getNumMinions() == 2);
		assertTrue("test1_6", board.data_.getCurrentPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue("test1_7", board.data_.getCurrentPlayer().getMinions().get(1).getHealth() == health1);
		assertTrue("test1_8", board.data_.getWaitingPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue("test1_9", board.data_.getWaitingPlayer().getMinions().get(1).getHealth() == health1);
	}

	@Test
	public void test2() throws HSException {
		HolySmite hs = new HolySmite();
		board.data_.placeCardHandCurrentPlayer(hs);
		
		Minion target = board.data_.getCharacter(board.data_.getCurrentPlayer(), 1);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		theCard.useOn(board.data_.getCurrentPlayer(), target, board, null, null);
		
		assertTrue("test2_1", board.data_.getNumCards_hand() == 0);
		assertTrue("test2_2", board.data_.getCurrentPlayer().getNumMinions() == 1);
		assertTrue("test2_3", board.data_.getWaitingPlayer().getNumMinions() == 2);
		assertTrue("test2_4", board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue("test2_5", board.data_.getWaitingPlayerHero().getHealth() == 30);
		assertTrue("test2_6", board.data_.getCurrentPlayer().getMinions().get(0).getHealth() == health1);
		assertTrue("test2_8", board.data_.getWaitingPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue("test2_9", board.data_.getWaitingPlayer().getMinions().get(1).getHealth() == health1);
	}
	
	@Test
	public void test3() throws HSException {
		HolySmite hs = new HolySmite();
		board.data_.placeCardHandCurrentPlayer(hs);
		
		Minion target = board.data_.getCharacter(board.data_.getCurrentPlayer(), 2);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		theCard.useOn(board.data_.getCurrentPlayer(), target, board, null, null);
		
		assertTrue("test3_1", board.data_.getNumCards_hand() == 0);
		assertTrue("test3_2", board.data_.getCurrentPlayer().getNumMinions() == 2);
		assertTrue("test3_3", board.data_.getWaitingPlayer().getNumMinions() == 2);
		assertTrue("test3_4", board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue("test3_5", board.data_.getWaitingPlayerHero().getHealth() == 30);
		assertTrue("test3_6", board.data_.getCurrentPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue("test3_7", board.data_.getCurrentPlayer().getMinions().get(1).getHealth() == health1 - 2);
		assertTrue("test3_8", board.data_.getWaitingPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue("test3_9", board.data_.getWaitingPlayer().getMinions().get(1).getHealth() == health1);
	}
	
	@Test
	public void test4() throws HSException {
		HolySmite hs = new HolySmite();
		board.data_.placeCardHandCurrentPlayer(hs);
		
		Minion target = board.data_.getCharacter(board.data_.getWaitingPlayer(), 1);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		theCard.useOn(board.data_.getWaitingPlayer(), target, board, null, null);
		
		assertTrue("test4_1", board.data_.getNumCards_hand() == 0);
		assertTrue("test4_2", board.data_.getCurrentPlayer().getNumMinions() == 2);
		assertTrue("test4_3", board.data_.getWaitingPlayer().getNumMinions() == 1);
		assertTrue("test4_4", board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue("test4_5", board.data_.getWaitingPlayerHero().getHealth() == 30);
		assertTrue("test4_6", board.data_.getCurrentPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue("test4_7", board.data_.getCurrentPlayer().getMinions().get(1).getHealth() == health1);
		assertTrue("test4_8", board.data_.getWaitingPlayer().getMinions().get(0).getHealth() == health1);
	}

	@Test
	public void test5() throws HSException {
		HolySmite hs = new HolySmite();
		board.data_.placeCardHandCurrentPlayer(hs);
		
		Minion target = board.data_.getCharacter(board.data_.getWaitingPlayer(), 2);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		theCard.useOn(board.data_.getWaitingPlayer(), target, board, null, null);
		
		assertTrue("test5_1", board.data_.getNumCards_hand() == 0);
		assertTrue("test5_2", board.data_.getCurrentPlayer().getNumMinions() == 2);
		assertTrue("test5_3", board.data_.getWaitingPlayer().getNumMinions() == 2);
		assertTrue("test5_4", board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue("test5_5", board.data_.getWaitingPlayerHero().getHealth() == 30);
		assertTrue("test5_6", board.data_.getCurrentPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue("test5_7", board.data_.getCurrentPlayer().getMinions().get(1).getHealth() == health1);
		assertTrue("test5_8", board.data_.getWaitingPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue("test5_9", board.data_.getWaitingPlayer().getMinions().get(1).getHealth() == health1 - 2);
	}
}
