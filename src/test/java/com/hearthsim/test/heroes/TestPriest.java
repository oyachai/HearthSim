package com.hearthsim.test.heroes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.ChillwindYeti;
import com.hearthsim.card.minion.heroes.Priest;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.factory.DepthBoardStateFactory;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestPriest {

	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel(new Priest(), new TestHero()));

		Minion minion0_0 = new ChillwindYeti();
		Minion minion0_1 = new ChillwindYeti();
		Minion minion1_0 = new ChillwindYeti();
		Minion minion1_1 = new ChillwindYeti();

		board.data_.placeCardHandCurrentPlayer(minion0_1);
		board.data_.placeCardHandCurrentPlayer(minion0_0);

		board.data_.placeCardHandWaitingPlayer(minion1_1);
		board.data_.placeCardHandWaitingPlayer(minion1_0);

		Card cards[] = new Card[10];
		for(int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}

		deck = new Deck(cards);

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
	public void testAiHealAfterTrading() throws HSException {
		BoardStateFactoryBase factory = new DepthBoardStateFactory(null, null, 2000000000, true);
		HearthTreeNode tree = new HearthTreeNode(board.data_);
		BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
		try {
			tree = factory.doMoves(tree, ai0.scorer);
		} catch(HSException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		// best move is to kill one enemy yeti with 2 of your own yeti and heal
		// one of your own yeti
		HearthTreeNode bestPlay = tree.findMaxOfFunc(ai0.scorer);

		assertFalse(tree == null);
		assertEquals(bestPlay.data_.getNumCards_hand(), 0);
		assertEquals(bestPlay.data_.getCurrentPlayer().getNumMinions(), 2);
		assertEquals(bestPlay.data_.getWaitingPlayer().getNumMinions(), 1);
		assertEquals(bestPlay.data_.getCurrentPlayer().getMana(), 6);
		assertEquals(bestPlay.data_.getWaitingPlayer().getMana(), 8);
		assertEquals(bestPlay.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(bestPlay.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(bestPlay.data_.getCurrentPlayer().getMinions().get(0).getHealth(), 3);
		assertEquals(bestPlay.data_.getCurrentPlayer().getMinions().get(1).getHealth(), 1);
		assertEquals(bestPlay.data_.getWaitingPlayer().getMinions().get(0).getHealth(), 5);

		assertEquals(bestPlay.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 4);
		assertEquals(bestPlay.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 4);
		assertEquals(bestPlay.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 4);

	}

	@Test
	public void testAiHealBeforeTrading() throws HSException {

		// one of your yeti is damaged
		PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).setHealth((byte)3);

		BoardStateFactoryBase factory = new DepthBoardStateFactory(null, null, 2000000000, true);
		HearthTreeNode tree = new HearthTreeNode(board.data_);
		BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
		try {
			tree = factory.doMoves(tree, ai0.scorer);
		} catch(HSException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		// best move is to heal the damaged yeti and attack with both to kill
		// one of the enemy's yeti
		HearthTreeNode bestPlay = tree.findMaxOfFunc(ai0.scorer);

		assertFalse(tree == null);
		assertEquals(bestPlay.data_.getNumCards_hand(), 0);
		assertEquals(bestPlay.data_.getCurrentPlayer().getNumMinions(), 2);
		assertEquals(bestPlay.data_.getWaitingPlayer().getNumMinions(), 1);
		assertEquals(bestPlay.data_.getCurrentPlayer().getMana(), 6);
		assertEquals(bestPlay.data_.getWaitingPlayer().getMana(), 8);
		assertEquals(bestPlay.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(bestPlay.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(bestPlay.data_.getCurrentPlayer().getMinions().get(0).getHealth(), 1);
		assertEquals(bestPlay.data_.getCurrentPlayer().getMinions().get(1).getHealth(), 1);
		assertEquals(bestPlay.data_.getWaitingPlayer().getMinions().get(0).getHealth(), 5);

		assertEquals(bestPlay.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 4);
		assertEquals(bestPlay.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 4);
		assertEquals(bestPlay.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 4);

	}

	@Test
	public void testHeropowerAgainstOpponent() throws HSException {
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0); // Opponent
																				// hero
		target.setHealth((byte)20);

		Hero priest = board.data_.getCurrentPlayerHero();

		HearthTreeNode ret = priest.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertEquals(board, ret);

		assertEquals(board.data_.getCurrentPlayer().getMana(), 6);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 22);
	}

	@Test
	public void testHeropowerAgainstMinion() throws HSException {
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 1);
		target.setHealth((byte)1);

		Hero priest = board.data_.getCurrentPlayerHero();

		HearthTreeNode ret = priest.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertEquals(board, ret);

		assertEquals(board.data_.getCurrentPlayer().getMana(), 6);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 3);
	}

	@Test
	public void testHeropowerAgainstSelf() throws HSException {
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0); // Self
		target.setHealth((byte)20);

		Hero priest = board.data_.getCurrentPlayerHero();

		HearthTreeNode ret = priest.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertEquals(board, ret);

		assertEquals(board.data_.getCurrentPlayer().getMana(), 6);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 22);
	}

	@Test
	public void testHeropowerAgainstOwnMinion() throws HSException {
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 1);
		target.setHealth((byte)1);

		Hero priest = board.data_.getCurrentPlayerHero();

		HearthTreeNode ret = priest.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertEquals(board, ret);

		assertEquals(board.data_.getCurrentPlayer().getMana(), 6);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 3);
	}
}
