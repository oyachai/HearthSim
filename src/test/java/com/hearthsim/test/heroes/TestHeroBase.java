package com.hearthsim.test.heroes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestHeroBase {

	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() {
		board = new HearthTreeNode(new BoardModel(new TestHero(), new TestHero()));
		
		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}
	
		deck = new Deck(cards);

		board.data_.getCurrentPlayer().setMana((byte)8);
		board.data_.getWaitingPlayer().setMana((byte)8);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)8);
		board.data_.getWaitingPlayer().setMaxMana((byte)8);
		
		board.data_.resetMana();
		board.data_.resetMinions();
		
	}

	@Test
	public void testHeropowerIsMarkedUsed() throws HSException {
		Hero hero = board.data_.getCurrentPlayerHero();
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0); // Hero
		HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertNotNull(ret);
		assertTrue(hero.hasBeenUsed());
	}

	@Test
	public void testHeropowerSubtractsMana() throws HSException {
		Hero hero = board.data_.getCurrentPlayerHero();
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0); // Hero
		HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertNotNull(ret);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 6);
	}

	@Test
	@Ignore("Existing bug")
	public void testHeropowerCannotBeUsedTwice() throws HSException {
		Hero hero = board.data_.getCurrentPlayerHero();
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0); // Hero
		HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertNotNull(ret);

		target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0); // Hero
		ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertNull(ret);

		assertTrue(hero.hasBeenUsed());
		assertEquals(board.data_.getCurrentPlayer().getMana(), 6);
	}

	@Test
	public void testNotEnoughMana() throws HSException {
		Hero hero = board.data_.getCurrentPlayerHero();
		board.data_.getCurrentPlayer().setMana((byte)1);

		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0); // Hero
		HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertNull(ret);

		assertFalse(hero.hasBeenUsed());
		assertEquals(board.data_.getCurrentPlayer().getMana(), 1);
	}
}
