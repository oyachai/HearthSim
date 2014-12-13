package com.hearthsim.test.heroes;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.card.minion.heroes.Warlock;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.card.spellcard.concrete.WildGrowth;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestWarlock {

	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel(new Warlock(), new TestHero()));

		Minion minion0_0 = new BoulderfistOgre();
		Minion minion0_1 = new RaidLeader();
		Minion minion1_0 = new BoulderfistOgre();
		Minion minion1_1 = new RaidLeader();

		board.data_.placeCardHandCurrentPlayer(minion0_0);
		board.data_.placeCardHandCurrentPlayer(minion0_1);

		board.data_.placeCardHandWaitingPlayer(minion1_0);
		board.data_.placeCardHandWaitingPlayer(minion1_1);

		Card cards[] = new Card[30];
		for(int index = 0; index < 30; ++index) {
			cards[index] = new TheCoin();
		}

		deck = new Deck(cards);

		Card fb = new WildGrowth();
		board.data_.placeCardHandCurrentPlayer(fb);

		board.data_.getCurrentPlayer().setMana((byte)9);
		board.data_.getWaitingPlayer().setMana((byte)9);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)8);
		board.data_.getWaitingPlayer().setMaxMana((byte)8);
		
		HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
		tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER,
				tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);
		tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER,
				tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);

		board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
		board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayerHero(),
				board, deck, null);
		board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayerHero(),
				board, deck, null);

		board.data_.resetMana();
		board.data_.resetMinions();

	}

	@Test
	public void testHeropower() throws HSException {
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		Hero warrior = board.data_.getCurrentPlayerHero();

		HearthTreeNode ret = warrior.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertNotEquals(board, ret);
		assertTrue(ret instanceof CardDrawNode);

		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 6);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 28);

		assertEquals(ret.data_.getNumCards_hand(), 1);
		assertEquals(((CardDrawNode)ret).getNumCardsToDraw(), 1);
		assertEquals(ret.data_.getCurrentPlayer().getMana(), 6);
		assertEquals(ret.data_.getCurrentPlayerHero().getHealth(), 28);
	}

	@Test
	public void testHeropowerCannotTargetMinion() throws HSException {
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 2);
		Hero warrior = board.data_.getCurrentPlayerHero();

		HearthTreeNode ret = warrior.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertNull(ret);

		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 8);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 7);
	}

	@Test
	public void testHeropowerCannotTargetOpponent() throws HSException {
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		Hero warrior = board.data_.getCurrentPlayerHero();

		HearthTreeNode ret = warrior.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertNull(ret);

		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 8);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 8);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
	}

	@Test
	public void testAiCardDrawScore() throws HSException {
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		Minion minion = PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0);
		HearthTreeNode ret = minion.attack(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertEquals(board, ret);
		
		board.data_.getCurrentPlayer().setDeckPos((byte)30);

		target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		Hero hero = board.data_.getCurrentPlayerHero();
		ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertEquals(board.data_.getNumCards_hand(), 1);

		assertTrue(ret instanceof CardDrawNode);
		assertEquals(((CardDrawNode)ret).getNumCardsToDraw(), 1);

		BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
		double cardDrawScore = ((CardDrawNode)ret).cardDrawScore(deck, ai0.scorer);
		assertTrue(cardDrawScore < 0.0);
	}
}
