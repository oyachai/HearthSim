package com.hearthsim.test.minion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.AcolyteOfPain;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.SilverHandRecruit;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.card.spellcard.concrete.Assassinate;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.util.HearthActionBoardPair;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestAcolyteOfPain {

	private HearthTreeNode board;
	private Deck deck;

	private AcolyteOfPain acolyteOnBoard;

	@Before
	public void setup() throws HSException {
		Card cards[] = new Card[10];
		for(int index = 0; index < 10; ++index) {
			cards[index] = new BloodfenRaptor();
		}

		deck = new Deck(cards);
		PlayerModel playerModel0 = new PlayerModel((byte)0, "player0", new TestHero(), deck);
		PlayerModel playerModel1 = new PlayerModel((byte)1, "player1", new TestHero(), deck);

		board = new HearthTreeNode(new BoardModel(playerModel0, playerModel1));

		acolyteOnBoard = new AcolyteOfPain();
		Minion minion1_0 = new SilverHandRecruit();

		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, acolyteOnBoard);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);

		AcolyteOfPain acolyteInHand = new AcolyteOfPain();
		board.data_.placeCardHandCurrentPlayer(acolyteInHand);

		board.data_.getCurrentPlayer().setMana((byte)7);
		board.data_.getWaitingPlayer().setMana((byte)7);
	}

	@Test
	public void testAiDrawsCardFromAcolyte() throws HSException {
		board.data_.getCurrentPlayer().setMana((byte)1);

		BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
		List<HearthActionBoardPair> ab = ai0.playTurn(0, board.data_);
		BoardModel resBoard = ab.get(ab.size() - 1).board;

		assertEquals(resBoard.getNumCardsHandCurrentPlayer(), 2); // 1 card drawn from AcolyteOfPain, not enough mana to play it
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getNumMinions(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(resBoard).getNumMinions(), 0); // 1 minion should have been killed
		assertEquals(resBoard.getCurrentPlayer().getMana(), 1); // 0 mana used
	}

	@Test
	public void testAiDrawsAndPlaysCardFromAcolyte() throws HSException {
		board.data_.getCurrentPlayer().setMana((byte)3);

		BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
		List<HearthActionBoardPair> ab = ai0.playTurn(0, board.data_);
		BoardModel resBoard = ab.get(ab.size() - 1).board;

		assertEquals(resBoard.getNumCardsHandCurrentPlayer(), 1); // 1 card drawn from AcolyteOfPain, then played the Bloodfen Raptor
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getNumMinions(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(resBoard).getNumMinions(), 0); // 1 minion should have been killed
		assertEquals(resBoard.getCurrentPlayer().getMana(), 1); // 2 mana used... it's better to put down a Bloodfen Raptor than an Acolyte of Pain
	}

	@Test
	public void testDamagingEnemyAcolyteDrawsCard() throws HSException {
		board.data_.getCurrentPlayer().setMana((byte)1);

		board.data_.removeMinion(PlayerSide.WAITING_PLAYER, 0);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new AcolyteOfPain());

		assertEquals(board.data_.getNumCardsHandWaitingPlayer(), 0);

		BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
		List<HearthActionBoardPair> ab = ai0.playTurn(0, board.data_);
		BoardModel resBoard = ab.get(ab.size() - 1).board;

		assertEquals(resBoard.getNumCardsHandCurrentPlayer(), 2); // 1 card drawn from AcolyteOfPain
		assertEquals(resBoard.getNumCardsHandWaitingPlayer(), 1); // 1 card drawn from AcolyteOfPain. The Acolytes smack into each other.
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getNumMinions(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(resBoard).getNumMinions(), 1);
	}

	@Test
	public void testDivineShieldPreventsDraw() throws HSException {
		acolyteOnBoard.setDivineShield(true);
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 1);
		HearthTreeNode ret = acolyteOnBoard.attack(PlayerSide.WAITING_PLAYER, target, board, deck, deck);
		assertEquals(board, ret);

		assertFalse(board instanceof CardDrawNode);
		assertEquals(board.data_.getNumCardsHandCurrentPlayer(), 1);
	}

	@Test
	public void testDestroyingPreventsDraw() throws HSException {
		board.data_.getCurrentPlayer().setMana((byte)5);

		AcolyteOfPain enemyAcolyte = new AcolyteOfPain();
		board.data_.removeMinion(PlayerSide.WAITING_PLAYER, 0);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, enemyAcolyte);

		assertEquals(board.data_.getNumCardsHandWaitingPlayer(), 0);
		Assassinate assassinate = new Assassinate();
		board.data_.placeCardHandCurrentPlayer(assassinate);
		HearthTreeNode ret = assassinate.useOn(PlayerSide.WAITING_PLAYER, enemyAcolyte, board, deck, deck);
		assertEquals(board, ret);

		assertFalse(board instanceof CardDrawNode);
		assertEquals(board.data_.getNumCardsHandCurrentPlayer(), 1);
		assertEquals(board.data_.getNumCardsHandWaitingPlayer(), 0);

		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 0);
	}
}
