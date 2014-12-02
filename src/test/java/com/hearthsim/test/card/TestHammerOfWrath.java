package com.hearthsim.test.card;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.concrete.HammerOfWrath;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestHammerOfWrath {

	private HearthTreeNode board;
	private Deck deck;
	private static final byte mana = 2;
	private static final byte attack0 = 2;
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
	}
	
	@Test
	public void test0() throws HSException {
		HammerOfWrath fb = new HammerOfWrath();
		board.data_.placeCardHandCurrentPlayer(fb);
		
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.getCardAction().useOn(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertFalse(ret == null);
		assertEquals(ret.data_.getNumCards_hand(), 0);
		assertTrue(ret instanceof CardDrawNode);
		assertEquals(((CardDrawNode)ret).getNumCardsToDraw(), 1);
		assertTrue(ret.data_.getCurrentPlayer().getNumMinions() == 2);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(ret).getNumMinions() == 2);
		assertTrue(ret.data_.getCurrentPlayerHero().getHealth() == 27);
		assertTrue(ret.data_.getWaitingPlayerHero().getHealth() == 30);
		assertTrue(ret.data_.getCurrentPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue(ret.data_.getCurrentPlayer().getMinions().get(1).getHealth() == health1 - 1);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(0).getHealth() == health0);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(1).getHealth() == health1 - 1);
		assertTrue(ret.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack() == attack0);
		assertTrue(ret.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack() == attack0);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(0).getTotalAttack() == attack0);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(1).getTotalAttack() == attack0);
		assertFalse(ret.data_.getCurrentPlayer().getMinions().get(0).getCharge());
		assertFalse(ret.data_.getCurrentPlayer().getMinions().get(1).getCharge());
		assertFalse(ret.data_.getCurrentPlayer().getMinions().get(0).getFrozen());
		assertFalse(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(0).getFrozen());
	}
	
	@Test
	public void test1() throws HSException {
		HammerOfWrath fb = new HammerOfWrath();
		board.data_.placeCardHandCurrentPlayer(fb);
		
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 1);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.getCardAction().useOn(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertFalse(ret == null);
		assertEquals(ret.data_.getNumCards_hand(), 0);
		assertTrue(ret instanceof CardDrawNode);
		assertEquals(((CardDrawNode)ret).getNumCardsToDraw(), 1);

		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions() == 2);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions() == 1);
		assertTrue(board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getWaitingPlayerHero().getHealth() == 30);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth() == health0);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth() == health1 - 1);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth() == health1 - 1);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack() == attack0);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack() == attack0);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack() == attack0);
	}
}
