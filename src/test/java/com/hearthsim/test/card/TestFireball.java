package com.hearthsim.test.card;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.concrete.Fireball;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestFireball {

	private HearthTreeNode board;
	private static final byte mana = 2;
	private static final byte attack0 = 2;
	private static final byte health0 = 5;
	private static final byte health1 = 7;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode( new BoardModel() );

		Minion minion0_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion0_1 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1);
		Minion minion1_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion1_1 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1);
		
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_1);
		
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_1);
				
	}
	
	@Test
	public void test0() throws HSException {
		Fireball fb = new Fireball();
		board.data_.placeCardHandCurrentPlayer(fb);
		
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		theCard.getCardAction().useOn(PlayerSide.CURRENT_PLAYER, target, board, null, null);
		
		assertTrue("test0_1", board.data_.getNumCards_hand() == 0);
		assertTrue("test0_2", PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions() == 2);
		assertTrue("test0_3", PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions() == 2);
		assertTrue("test0_4", board.data_.getCurrentPlayerHero().getHealth() == 24);
		assertTrue("test0_5", board.data_.getWaitingPlayerHero().getHealth() == 30);
		assertTrue("test0_6", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth() == health0);
		assertTrue("test0_7", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth() == health1);
		assertTrue("test0_8", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth() == health0);
		assertTrue("test0_9", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth() == health1);
	}

	@Test
	public void test1() throws HSException {
		Fireball fb = new Fireball();
		board.data_.placeCardHandCurrentPlayer(fb);
		
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		theCard.getCardAction().useOn(PlayerSide.WAITING_PLAYER, target, board, null, null);
		
		assertTrue("test1_1", board.data_.getNumCards_hand() == 0);
		assertTrue("test1_2", board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue("test1_3", board.data_.getWaitingPlayerHero().getHealth() == 24);
		assertTrue("test1_4", PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions() == 2);
		assertTrue("test1_5", PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions() == 2);
		assertTrue("test1_6", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth() == health0);
		assertTrue("test1_7", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth() == health1);
		assertTrue("test1_8", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth() == health0);
		assertTrue("test1_9", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth() == health1);
	}

	@Test
	public void test2() throws HSException {
		Fireball fb = new Fireball();
		board.data_.placeCardHandCurrentPlayer(fb);
		
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 1);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		theCard.getCardAction().useOn(PlayerSide.CURRENT_PLAYER, target, board, null, null);
		
		assertTrue("test2_1", board.data_.getNumCards_hand() == 0);
		assertTrue("test2_2", PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions() == 1);
		assertTrue("test2_3", PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions() == 2);
		assertTrue("test2_4", board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue("test2_5", board.data_.getWaitingPlayerHero().getHealth() == 30);
		assertTrue("test2_6", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth() == health1);
		assertTrue("test2_8", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth() == health0);
		assertTrue("test2_9", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth() == health1);
	}
	
	@Test
	public void test3() throws HSException {
		Fireball fb = new Fireball();
		board.data_.placeCardHandCurrentPlayer(fb);
		
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 2);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		theCard.getCardAction().useOn(PlayerSide.CURRENT_PLAYER, target, board, null, null);
		
		assertTrue("test3_1", board.data_.getNumCards_hand() == 0);
		assertTrue("test3_2", PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions() == 2);
		assertTrue("test3_3", PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions() == 2);
		assertTrue("test3_4", board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue("test3_5", board.data_.getWaitingPlayerHero().getHealth() == 30);
		assertTrue("test3_6", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth() == health0);
		assertTrue("test3_7", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth() == health1 - 6);
		assertTrue("test3_8", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth() == health0);
		assertTrue("test3_9", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth() == health1);
	}
	
	@Test
	public void test4() throws HSException {
		Fireball fb = new Fireball();
		board.data_.placeCardHandCurrentPlayer(fb);
		
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 1);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		theCard.getCardAction().useOn(PlayerSide.WAITING_PLAYER, target, board, null, null);
		
		assertTrue("test4_1", board.data_.getNumCards_hand() == 0);
		assertTrue("test4_2", PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions() == 2);
		assertTrue("test4_3", PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions() == 1);
		assertTrue("test4_4", board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue("test4_5", board.data_.getWaitingPlayerHero().getHealth() == 30);
		assertTrue("test4_6", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth() == health0);
		assertTrue("test4_7", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth() == health1);
		assertTrue("test4_8", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth() == health1);
	}

	@Test
	public void test5() throws HSException {
		Fireball fb = new Fireball();
		board.data_.placeCardHandCurrentPlayer(fb);
		
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 2);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		theCard.getCardAction().useOn(PlayerSide.WAITING_PLAYER, target, board, null, null);
		
		assertTrue("test5_1", board.data_.getNumCards_hand() == 0);
		assertTrue("test5_2", PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions() == 2);
		assertTrue("test5_3", PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions() == 2);
		assertTrue("test5_4", board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue("test5_5", board.data_.getWaitingPlayerHero().getHealth() == 30);
		assertTrue("test5_6", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth() == health0);
		assertTrue("test5_7", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth() == health1);
		assertTrue("test5_8", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth() == health0);
		assertTrue("test5_9", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth() == health1 - 6);
	}
}
