package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.AcidicSwampOoze;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.card.weapon.concrete.FieryWarAxe;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestAcidicSwampOoze {

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

		AcidicSwampOoze fb = new AcidicSwampOoze();
		board.data_.placeCardHandCurrentPlayer(fb);

		board.data_.getCurrentPlayer().setMana((byte)4);
		board.data_.getWaitingPlayer().setMana((byte)4);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)4);
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
		
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 2);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.getCardAction().useOn(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1 - 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getHealth(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1 - 1);
	}
	
	@Test
	public void test3() throws HSException {
		
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		HearthTreeNode fl = new HearthTreeNode(board.data_.flipPlayers());
		fl.data_.placeCardHandCurrentPlayer(new FieryWarAxe());
		fl = fl.data_.getCurrentPlayerCardHand(0).getCardAction().useOn(PlayerSide.CURRENT_PLAYER, target, fl, deck, null);
		board = new HearthTreeNode(fl.data_.flipPlayers());
		
		assertEquals(board.data_.getCurrentPlayerHero().getTotalAttack(), 0);
		assertEquals(board.data_.getCurrentPlayerHero().getWeaponCharge(), 0);
		assertEquals(board.data_.getWaitingPlayerHero().getTotalAttack(), 3);
		assertEquals(board.data_.getWaitingPlayerHero().getWeaponCharge(), 2);

		target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 2);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.getCardAction().useOn(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);

		assertEquals(board.data_.getCurrentPlayerHero().getTotalAttack(), 0);
		assertEquals(board.data_.getCurrentPlayerHero().getWeaponCharge(), 0);
		assertEquals(board.data_.getWaitingPlayerHero().getTotalAttack(), 0);
		assertEquals(board.data_.getWaitingPlayerHero().getWeaponCharge(), 0);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1 - 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getHealth(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1 - 1);
	}
}
