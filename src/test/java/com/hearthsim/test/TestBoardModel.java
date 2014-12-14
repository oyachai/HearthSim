package com.hearthsim.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.AbusiveSergeant;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.minion.concrete.ScarletCrusader;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestBoardModel {

	private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

	private Deck deck0;
	private Deck deck1;

	private static Minion CreateMinionAlpha() {
		return new Minion("alpha", (byte)2, (byte)1, (byte)4, (byte)1, (byte)4, (byte)4);
	}

	private static Minion CreateMinionBeta() {
		return new Minion("beta", (byte)2, (byte)1, (byte)5, (byte)1, (byte)5, (byte)4);
	}

	private static Minion CreateMinionCharlie() {
		return new Minion("charlie", (byte)2, (byte)1, (byte)4, (byte)1, (byte)4, (byte)4);
	}

	@Test
	public void testEqualsEmpty() throws HSException {
		BoardModel board1 = new BoardModel();
		BoardModel board2 = new BoardModel();
		assertEquals(board1, board2);
	}

	@Test
	public void testEqualsWithCardHand() throws HSException {
		BoardModel board1 = new BoardModel();
		BoardModel board2 = new BoardModel();
		BoardModel board3 = new BoardModel();

		board1.placeCardHandCurrentPlayer(TestBoardModel.CreateMinionAlpha());
		board3.placeCardHandCurrentPlayer(TestBoardModel.CreateMinionAlpha());

		assertNotEquals(board1, board2);
		assertEquals(board1, board3);
	}

	@Test
	public void testEqualsWithPlacedMinions() throws HSException {
		BoardModel board1 = new BoardModel();
		BoardModel board2 = new BoardModel();
		BoardModel board3 = new BoardModel();

		board1.placeMinion(PlayerSide.CURRENT_PLAYER, TestBoardModel.CreateMinionAlpha());
		board3.placeMinion(PlayerSide.CURRENT_PLAYER, TestBoardModel.CreateMinionAlpha());
		assertEquals(board1, board3);

		board2.placeMinion(PlayerSide.CURRENT_PLAYER, TestBoardModel.CreateMinionAlpha());
		board2.placeMinion(PlayerSide.CURRENT_PLAYER, TestBoardModel.CreateMinionBeta());
		assertNotEquals(board1, board2);
	}

	@Test
	public void testEqualsWithMultiplePlacedMinions() throws HSException {
		BoardModel board1 = new BoardModel();
		BoardModel board3 = new BoardModel();

		board1.placeMinion(PlayerSide.CURRENT_PLAYER, TestBoardModel.CreateMinionAlpha());
		board3.placeMinion(PlayerSide.CURRENT_PLAYER, TestBoardModel.CreateMinionAlpha());

		board1.placeMinion(PlayerSide.WAITING_PLAYER, TestBoardModel.CreateMinionAlpha());
		board3.placeMinion(PlayerSide.WAITING_PLAYER, TestBoardModel.CreateMinionAlpha());

		board1.placeMinion(PlayerSide.WAITING_PLAYER, TestBoardModel.CreateMinionCharlie());
		board3.placeMinion(PlayerSide.WAITING_PLAYER, TestBoardModel.CreateMinionCharlie());
		assertEquals(board1, board3);
	}

	@Test
	public void testBoardState() throws HSException {

		int numBoards = 2000;
		BoardModel[] boards = new BoardModel[numBoards];

		int numCards1 = 1;
		int numCards2 = 1;
		int numCards3 = 2;

		Card[] cards1 = new Card[numCards1];
		Card[] cards2 = new Card[numCards2];
		Card[] cards3 = new Card[numCards3];

		for(int i = 0; i < numCards1; ++i) {
			byte attack = (byte)((int)(Math.random() * 6) + 1);
			byte health = (byte)((int)(Math.random() * 2) + 1);
			byte mana = (byte)((int)(0.5 * (attack + health)));

			cards1[i] = new Minion("" + i, mana, attack, health, attack, health, health);
		}

		for(int i = 0; i < numCards2; ++i) {
			byte attack = (byte)((int)(Math.random() * 6) + 1);
			byte health = (byte)((int)(Math.random() * 2) + 1);
			byte mana = (byte)((int)(0.5 * (attack + health)));

			cards2[i] = new Minion("" + i, mana, attack, health, attack, health, health);
		}

		for(int i = 0; i < numCards3; ++i) {
			byte attack = (byte)((int)(Math.random() * 6) + 1);
			byte health = (byte)((int)(Math.random() * 2) + 1);
			byte mana = (byte)((int)(0.5 * (attack + health)));

			cards3[i] = new Minion("" + i, mana, attack, health, attack, health, health);
		}

		for(int i = 0; i < numBoards; ++i) {
			boards[i] = new BoardModel();

			int nh = (int)(Math.random() * 1) + 1;
			int nm1 = (int)(Math.random() * 1) + 1;
			int nm2 = (int)(Math.random() * 2) + 1;

			for(int j = 0; j < nh; ++j) {
				boards[i].placeCardHandCurrentPlayer(cards1[(int)(Math.random() * numCards1)]);
			}

			for(int j = 0; j < nm1; ++j) {
				boards[i].placeMinion(PlayerSide.CURRENT_PLAYER, (Minion)cards2[(int)(Math.random() * numCards2)]);
			}

			for(int j = 0; j < nm2; ++j) {
				boards[i].placeMinion(PlayerSide.WAITING_PLAYER, (Minion)cards3[(int)(Math.random() * numCards3)]);
			}
		}

		long nT = 0;
		long nA = 0;
		for(int i = 0; i < numBoards; ++i) {
			for(int j = i + 1; j < numBoards; ++j) {
				boolean res = boards[i].equals(boards[j]);
				if(res)
					nT = nT + 1;
				nA = nA + 1;
			}
		}
		log.info("t frac = " + (double)nT / (double)nA);
	}

	@Test
	public void testBoardModelEquals() throws HSException {

		HearthTreeNode board = new HearthTreeNode(new BoardModel());

		Minion minion0_0 = new BoulderfistOgre();
		Minion minion0_1 = new RaidLeader();
		Minion minion0_2 = new AbusiveSergeant();
		Minion minion1_0 = new BoulderfistOgre();
		Minion minion1_1 = new RaidLeader();
		Minion minion1_2 = new ScarletCrusader();

		board.data_.placeCardHandCurrentPlayer(minion0_0);
		board.data_.placeCardHandCurrentPlayer(minion0_1);
		board.data_.placeCardHandCurrentPlayer(minion0_2);

		board.data_.placeCardHandWaitingPlayer(minion1_0);
		board.data_.placeCardHandWaitingPlayer(minion1_1);
		board.data_.placeCardHandWaitingPlayer(minion1_2);

		board.data_.getCurrentPlayer().setMana((byte)8);
		board.data_.getWaitingPlayer().setMana((byte)8);

		board.data_.getCurrentPlayer().setMaxMana((byte)8);
		board.data_.getWaitingPlayer().setMaxMana((byte)8);

		HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
		tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, 0, tmpBoard, deck1, deck0);
		tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER,
				tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck1, deck0);
		tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER,
				tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck1, deck0);

		board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
		board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayerHero(),
				board, deck0, deck1);
		board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayerHero(),
				board, deck0, deck1);

		board.data_.resetMana();
		board.data_.resetMinions();

		assertTrue(board.data_.equals(board.data_));
		assertTrue(board.data_.equals(board.data_.deepCopy()));
		assertEquals(board.data_.hashCode(), board.data_.deepCopy().hashCode());
		assertFalse(board.data_.equals(board.data_.flipPlayers().deepCopy()));
	}
}
