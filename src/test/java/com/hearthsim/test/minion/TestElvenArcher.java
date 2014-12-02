package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.Abomination;
import com.hearthsim.card.minion.concrete.ElvenArcher;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestElvenArcher {
	private HearthTreeNode board;
	private Deck deck;
	private static final byte mana = 2;
	private static final byte attack0 = 5;
	private static final byte health0 = 3;
	private static final byte health1 = 7;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel());

        //public Minion(String name, byte mana, byte attack, byte health, byte baseAttack, byte baseHealth, byte maxHealth) {
		Minion minion0_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion0_1 = new Minion("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);
		Minion minion1_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion1_1 = new Minion("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);
		Minion minion1_2 = new Abomination();
		
		minion1_2.setHealth((byte)1);
		
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_1);
		
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_1);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_2);
		
		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}
	
		deck = new Deck(cards);

		Minion fb = new ElvenArcher();
		board.data_.placeCardHandCurrentPlayer(fb);

		board.data_.getCurrentPlayer().setMana((byte)7);
		board.data_.getWaitingPlayer().setMana((byte)4);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)7);
		board.data_.getWaitingPlayer().setMaxMana((byte)4);
		
	}
	
	@Test
	public void test0() throws HSException {
		
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.getCardAction().useOn(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		
		assertTrue(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1 - 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1 - 1);
	}
	
	@Test
	public void test2() throws HSException {
		
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 1);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.getCardAction().useOn(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 6);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 4);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getHealth(), health1 - 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1 - 1);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), attack0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalAttack(), attack0);
		
		assertEquals(board.numChildren(), 7);
		
		{
			HearthTreeNode child = board.getChildren().get(0);
			assertEquals(child.data_.getNumCards_hand(), 0);
			assertEquals(child.data_.getCurrentPlayer().getNumMinions(), 3);
			assertEquals(child.data_.getWaitingPlayer().getNumMinions(), 3);
			assertEquals(child.data_.getCurrentPlayer().getMana(), 6);
			assertEquals(child.data_.getWaitingPlayer().getMana(), 4);
			assertEquals(child.data_.getCurrentPlayerHero().getHealth(), 29);
			assertEquals(child.data_.getWaitingPlayerHero().getHealth(), 30);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(0).getHealth(), health0);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(1).getHealth(), 1);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(2).getHealth(), health1 - 1);
			assertEquals(child.data_.getWaitingPlayer().getMinions().get(0).getHealth(), health0);
			assertEquals(child.data_.getWaitingPlayer().getMinions().get(1).getHealth(), health1 - 1);
			
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), attack0);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 1);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), attack0);
		}
		
		{
			HearthTreeNode child = board.getChildren().get(1);
			assertEquals(child.data_.getNumCards_hand(), 0);
			assertEquals(child.data_.getCurrentPlayer().getNumMinions(), 3);
			assertEquals(child.data_.getWaitingPlayer().getNumMinions(), 3);
			assertEquals(child.data_.getCurrentPlayer().getMana(), 6);
			assertEquals(child.data_.getWaitingPlayer().getMana(), 4);
			assertEquals(child.data_.getCurrentPlayerHero().getHealth(), 30);
			assertEquals(child.data_.getWaitingPlayerHero().getHealth(), 29);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(0).getHealth(), health0);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(1).getHealth(), 1);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(2).getHealth(), health1 - 1);
			assertEquals(child.data_.getWaitingPlayer().getMinions().get(0).getHealth(), health0);
			assertEquals(child.data_.getWaitingPlayer().getMinions().get(1).getHealth(), health1 - 1);
			
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), attack0);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 1);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), attack0);
		}
		
		{
			HearthTreeNode child = board.getChildren().get(2);
			assertEquals(child.data_.getNumCards_hand(), 0);
			assertEquals(child.data_.getCurrentPlayer().getNumMinions(), 3);
			assertEquals(child.data_.getWaitingPlayer().getNumMinions(), 3);
			assertEquals(child.data_.getCurrentPlayer().getMana(), 6);
			assertEquals(child.data_.getWaitingPlayer().getMana(), 4);
			assertEquals(child.data_.getCurrentPlayerHero().getHealth(), 30);
			assertEquals(child.data_.getWaitingPlayerHero().getHealth(), 30);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(0).getHealth(), health0 - 1);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(1).getHealth(), 1);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(2).getHealth(), health1 - 1);
			assertEquals(child.data_.getWaitingPlayer().getMinions().get(0).getHealth(), health0);
			assertEquals(child.data_.getWaitingPlayer().getMinions().get(1).getHealth(), health1 - 1);
			
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), attack0);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 1);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), attack0);
		}		

		{
			HearthTreeNode child = board.getChildren().get(3);
			assertEquals(child.data_.getNumCards_hand(), 0);
			assertEquals(child.data_.getCurrentPlayer().getNumMinions(), 3);
			assertEquals(child.data_.getWaitingPlayer().getNumMinions(), 3);
			assertEquals(child.data_.getCurrentPlayer().getMana(), 6);
			assertEquals(child.data_.getWaitingPlayer().getMana(), 4);
			assertEquals(child.data_.getCurrentPlayerHero().getHealth(), 30);
			assertEquals(child.data_.getWaitingPlayerHero().getHealth(), 30);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(0).getHealth(), health0);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(1).getHealth(), 1);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(2).getHealth(), health1 - 1 - 1);
			assertEquals(child.data_.getWaitingPlayer().getMinions().get(0).getHealth(), health0);
			assertEquals(child.data_.getWaitingPlayer().getMinions().get(1).getHealth(), health1 - 1);
			
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), attack0);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 1);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), attack0);
		}		


		{
			HearthTreeNode child = board.getChildren().get(4);
			assertEquals(child.data_.getNumCards_hand(), 0);
			assertEquals(child.data_.getCurrentPlayer().getNumMinions(), 3);
			assertEquals(child.data_.getWaitingPlayer().getNumMinions(), 3);
			assertEquals(child.data_.getCurrentPlayer().getMana(), 6);
			assertEquals(child.data_.getWaitingPlayer().getMana(), 4);
			assertEquals(child.data_.getCurrentPlayerHero().getHealth(), 30);
			assertEquals(child.data_.getWaitingPlayerHero().getHealth(), 30);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(0).getHealth(), health0);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(1).getHealth(), 1);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(2).getHealth(), health1 - 1);
			assertEquals(child.data_.getWaitingPlayer().getMinions().get(0).getHealth(), health0 - 1);
			assertEquals(child.data_.getWaitingPlayer().getMinions().get(1).getHealth(), health1 - 1);
			
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), attack0);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 1);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), attack0);
		}		

		{
			HearthTreeNode child = board.getChildren().get(5);
			assertEquals(child.data_.getNumCards_hand(), 0);
			assertEquals(child.data_.getCurrentPlayer().getNumMinions(), 3);
			assertEquals(child.data_.getWaitingPlayer().getNumMinions(), 3);
			assertEquals(child.data_.getCurrentPlayer().getMana(), 6);
			assertEquals(child.data_.getWaitingPlayer().getMana(), 4);
			assertEquals(child.data_.getCurrentPlayerHero().getHealth(), 30);
			assertEquals(child.data_.getWaitingPlayerHero().getHealth(), 30);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(0).getHealth(), health0);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(1).getHealth(), 1);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(2).getHealth(), health1 - 1);
			assertEquals(child.data_.getWaitingPlayer().getMinions().get(0).getHealth(), health0);
			assertEquals(child.data_.getWaitingPlayer().getMinions().get(1).getHealth(), health1 - 1 - 1);
			
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), attack0);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 1);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), attack0);
		}		

		{
			HearthTreeNode child = board.getChildren().get(6);
			assertEquals(child.data_.getNumCards_hand(), 0);
			assertEquals(child.data_.getCurrentPlayer().getNumMinions(), 2);
			assertEquals(child.data_.getWaitingPlayer().getNumMinions(), 2);
			assertEquals(child.data_.getCurrentPlayer().getMana(), 6);
			assertEquals(child.data_.getWaitingPlayer().getMana(), 4);
			assertEquals(child.data_.getCurrentPlayerHero().getHealth(), 28);
			assertEquals(child.data_.getWaitingPlayerHero().getHealth(), 28);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(0).getTotalHealth(), health0 - 2);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(1).getTotalHealth(), health1 - 1 - 2);
			assertEquals(child.data_.getWaitingPlayer().getMinions().get(0).getTotalHealth(), health0 - 2);
			assertEquals(child.data_.getWaitingPlayer().getMinions().get(1).getTotalHealth(), health1 - 1 - 2);
			
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), attack0);
			assertEquals(child.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), attack0);
		}		

	}
}
