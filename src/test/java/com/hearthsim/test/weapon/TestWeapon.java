package com.hearthsim.test.weapon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.card.weapon.concrete.AssassinsBlade;
import com.hearthsim.card.weapon.concrete.FieryWarAxe;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestWeapon {

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
		for(int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}

		deck = new Deck(cards);

		FieryWarAxe fb = new FieryWarAxe();
		board.data_.placeCardHandCurrentPlayer(fb);

		board.data_.getCurrentPlayer().setMana((byte)2);
	}

	@Test
	public void testEquip() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, deck, null);

		assertEquals(board, ret);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertTrue(theCard.hasBeenUsed());
		//assertFalse(theCard.isInHand()); TODO existing bug

		assertEquals(board.data_.getCurrentPlayerHero().getWeaponCharge(), 2);
		assertEquals(board.data_.getCurrentPlayerHero().getTotalAttack(), 3);
	}

	@Test
	public void testHeroCanAttack() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, deck, null);

		Minion hero = ret.data_.getCurrentPlayerHero();
		Minion target = ret.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		ret = hero.attack(PlayerSide.WAITING_PLAYER, target, ret, deck, null);
		assertEquals(board, ret);

		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 27);

		assertEquals(board.data_.getCurrentPlayerHero().getAttack(), 3);
		assertEquals(board.data_.getCurrentPlayerHero().getWeaponCharge(), 1);
	}

	@Test
	public void testHeroCannotAttackTwice() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, deck, null);

		Minion hero = ret.data_.getCurrentPlayerHero();
		Minion target = ret.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		ret = hero.attack(PlayerSide.WAITING_PLAYER, target, ret, deck, null);

		hero = ret.data_.getCurrentPlayerHero();
		target = ret.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		ret = hero.attack(PlayerSide.WAITING_PLAYER, target, ret, deck, null);		
		assertNull(ret);

		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 27);

		assertEquals(board.data_.getCurrentPlayerHero().getAttack(), 3);
		assertEquals(board.data_.getCurrentPlayerHero().getWeaponCharge(), 1);
	}


	@Test
	public void testWeaponBreaks() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, deck, null);
		ret.data_.getCurrentPlayerHero().setWeaponCharge((byte)1);
		
		Minion hero = ret.data_.getCurrentPlayerHero();
		Minion target = ret.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		ret = hero.attack(PlayerSide.WAITING_PLAYER, target, ret, deck, null);
		assertEquals(board, ret);

		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 27);

		assertEquals(board.data_.getCurrentPlayerHero().getAttack(), 0);
		assertEquals(board.data_.getCurrentPlayerHero().getWeaponCharge(), 0);
	}

	@Test
	public void testDestroysEquippedWeapon() throws HSException {
		AssassinsBlade otherWeapon = new AssassinsBlade();
		board.data_.placeCardHandCurrentPlayer(otherWeapon);
		board.data_.getCurrentPlayer().setMana((byte)7);
		board.data_.getCurrentPlayerCardHand(1).useOn(PlayerSide.CURRENT_PLAYER, 0, board, deck, null);

		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, deck, null);
		assertEquals(board, ret);

		assertEquals(board.data_.getNumCards_hand(), 0);
		assertTrue(theCard.hasBeenUsed());
		//assertFalse(theCard.isInHand()); TODO existing bug

		assertEquals(board.data_.getCurrentPlayerHero().getWeaponCharge(), 2);
		assertEquals(board.data_.getCurrentPlayerHero().getTotalAttack(), 3);
	}

	@Test
	public void testCannotTargetMinion() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board, deck, null);
		assertNull(ret);

		assertFalse(theCard.hasBeenUsed());
		assertEquals(board.data_.getNumCards_hand(), 1);

		assertEquals(board.data_.getCurrentPlayerHero().getWeaponCharge(), 0);
		assertEquals(board.data_.getCurrentPlayerHero().getTotalAttack(), 0);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getAttack(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
	}

	@Test
	public void testCannotTargetOpponent() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 0, board, deck, null);
		assertNull(ret);

		assertFalse(theCard.hasBeenUsed());
		assertEquals(board.data_.getNumCards_hand(), 1);

		assertEquals(board.data_.getCurrentPlayerHero().getWeaponCharge(), 0);
		assertEquals(board.data_.getCurrentPlayerHero().getTotalAttack(), 0);

		assertEquals(board.data_.getWaitingPlayerHero().getWeaponCharge(), 0);
		assertEquals(board.data_.getWaitingPlayerHero().getTotalAttack(), 0);
	}
}
