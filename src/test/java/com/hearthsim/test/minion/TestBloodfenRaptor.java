package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestBloodfenRaptor {


	private HearthTreeNode board;
	private static final byte mana = 2;
	private static final byte attack0 = 2;
	private static final byte health0 = 3;
	private static final byte health1 = 7;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel());

		Minion minion0_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion0_1 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1);
		Minion minion1_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion1_1 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1);
		
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_1);
		
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_1);
		
		board.data_.getCurrentPlayer().setMana(10);
		board.data_.getCurrentPlayer().setMaxMana(10);
	}
	
	@Test
	public void test_deepCopy() {
		

		Minion card1 = new BloodfenRaptor();
		Minion card1_cloned = (Minion)card1.deepCopy();
		
		assertTrue(card1.equals(card1_cloned));
		assertTrue(card1_cloned.equals(card1));
		
		card1.setHealth((byte)(card1.getHealth() + 1));
		assertFalse(card1.equals(card1_cloned));
		assertFalse(card1_cloned.equals(card1));

		card1_cloned = (Minion)card1.deepCopy();
		assertTrue(card1.equals(card1_cloned));
		assertTrue(card1_cloned.equals(card1));

		card1.setAttack((byte)(card1.getTotalAttack() + 1));
		assertFalse(card1.equals(card1_cloned));
		assertFalse(card1_cloned.equals(card1));

		card1_cloned = (Minion)card1.deepCopy();
		assertTrue(card1.equals(card1_cloned));
		assertTrue(card1_cloned.equals(card1));
		
		card1.setDestroyOnTurnEnd(true);
		assertFalse(card1.equals(card1_cloned));
		assertFalse(card1_cloned.equals(card1));
		card1_cloned = (Minion)card1.deepCopy();
		assertTrue(card1.equals(card1_cloned));
		assertTrue(card1_cloned.equals(card1));

		card1.setDestroyOnTurnStart(true);
		assertFalse(card1.equals(card1_cloned));
		assertFalse(card1_cloned.equals(card1));
		card1_cloned = (Minion)card1.deepCopy();
		assertTrue(card1.equals(card1_cloned));
		assertTrue(card1_cloned.equals(card1));

	}
		
	@Test
	public void test1() throws HSException {
		BloodfenRaptor fb = new BloodfenRaptor();
		board.data_.placeCardHandCurrentPlayer(fb);
		
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, target, board, null, null);
		
		assertNull("test1_0", ret);
		assertEquals("test1_1", board.data_.getNumCards_hand(), 1);
		assertEquals("test1_2", PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals("test1_3", PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals("test1_4", board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals("test1_5", board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals("test1_6", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals("test1_7", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1);
		assertEquals("test1_8", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals("test1_9", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1);
	}
	
	@Test
	public void test2() throws HSException {
		BloodfenRaptor fb = new BloodfenRaptor();
		board.data_.placeCardHandCurrentPlayer(fb);
		
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, target, board, null, null);
		
		assertFalse("test2_0", ret == null);
		assertEquals("test2_1", board.data_.getNumCards_hand(), 0);
		assertEquals("test2_2", PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals("test2_3", PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals("test2_4", board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals("test2_5", board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals("test2_6", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), fb.getHealth());
		assertEquals("test2_7", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health0);
		assertEquals("test2_8", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getHealth(), health1);
		assertEquals("test2_9", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals("test2_10", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1);
		
		Minion theAttacker = PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0);
		target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		ret = theAttacker.useOn(PlayerSide.CURRENT_PLAYER, target, board, null, null);
		assertNull("test2", ret);

		target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		theAttacker = PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0);
		ret = theAttacker.useOn(PlayerSide.WAITING_PLAYER, target, board, null, null);
		assertNull("test2", ret);

		theAttacker = PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0);
		theAttacker.hasAttacked(false);
		theAttacker.hasBeenUsed(false);
		target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		ret = theAttacker.attack(PlayerSide.WAITING_PLAYER, target, board, null, null);
		assertFalse("test2_0", ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals("test2_4", board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals("test2_5", board.data_.getWaitingPlayerHero().getHealth(), 27);
		assertEquals("test2_6", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), fb.getHealth());
		assertEquals("test2_7", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health0);
		assertEquals("test2_8", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getHealth(), health1);
		assertEquals("test2_9", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals("test2_10", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1);
		assertTrue("test2", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).hasAttacked());
		
		theAttacker = PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0);
		theAttacker.hasAttacked(false);
		theAttacker.hasBeenUsed(false);
		target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 1);
		ret = theAttacker.attack(PlayerSide.WAITING_PLAYER, target, board, null, null);
		assertFalse("test2_0", ret == null);
		assertEquals("test2_1", board.data_.getNumCards_hand(), 0);
		assertEquals("test2_2", PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals("test2_3", PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 1);
		assertEquals("test2_4", board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals("test2_5", board.data_.getWaitingPlayerHero().getHealth(), 27);
		assertEquals("test2_7", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals("test2_8", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1);
		assertEquals("test2_9", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health1);
		
	}

	@Test
	public void test3() throws HSException {
		BloodfenRaptor fb = new BloodfenRaptor();
		board.data_.placeCardHandCurrentPlayer(fb);
		
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 1);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, target, board, null, null);
		
		assertFalse("test3_0", ret == null);
		assertEquals("test3_1", board.data_.getNumCards_hand(), 0);
		assertEquals("test3_2", PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals("test3_3", PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals("test3_4", board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals("test3_5", board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals("test3_6", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals("test3_7", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), fb.getHealth());
		assertEquals("test3_8", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getHealth(), health1);
		assertEquals("test3_9", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals("test3_10", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1);
	}

	@Test
	public void test4() throws HSException {
		BloodfenRaptor fb = new BloodfenRaptor();
		board.data_.placeCardHandCurrentPlayer(fb);
		
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 2);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, target, board, null, null);
		
		assertFalse("test4_0", ret == null);
		assertEquals("test4_1", board.data_.getNumCards_hand(), 0);
		assertEquals("test4_2", PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals("test4_3", PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals("test4_4", board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals("test4_5", board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals("test4_6", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals("test4_7", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1);
		assertEquals("test4_8", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getHealth(), fb.getHealth());
		assertEquals("test4_9", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals("test4_10", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1);
	}

	@Test
	public void test5() throws HSException {
		BloodfenRaptor fb = new BloodfenRaptor();
		board.data_.placeCardHandCurrentPlayer(fb);
		
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 1);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, target, board, null, null);
		
		assertNull("test5_0", ret);
		assertEquals("test5_1", board.data_.getNumCards_hand(), 1);
		assertEquals("test5_2", PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals("test5_3", PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals("test5_4", board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals("test5_5", board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals("test5_6", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals("test5_7", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1);
		assertEquals("test5_9", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals("test5_10", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1);
	}

	@Test
	public void test6() throws HSException {
		BloodfenRaptor fb = new BloodfenRaptor();
		board.data_.placeCardHandCurrentPlayer(fb);
		
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 2);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, target, board, null, null);
		
		assertNull("test6_0", ret);
		assertEquals("test6_1", board.data_.getNumCards_hand(), 1);
		assertEquals("test6_2", PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals("test6_3", PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals("test6_4", board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals("test6_5", board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals("test6_6", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals("test6_7", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1);
		assertEquals("test6_9", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals("test6_10", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1);
	}

	@Test
	public void test7() throws HSException {
		BloodfenRaptor fb = new BloodfenRaptor();
		board.data_.placeCardHandCurrentPlayer(fb);
		
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 2);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, target, board, null, null);
		
		assertNull("test7_0", ret);
		assertEquals("test7_1", board.data_.getNumCards_hand(), 1);
		assertEquals("test7_2", PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals("test7_3", PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals("test7_4", board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals("test7_5", board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals("test7_6", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals("test7_7", PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1);
		assertEquals("test7_9", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals("test7_10", PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1);
	}
}
