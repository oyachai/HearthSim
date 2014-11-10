package com.hearthsim.test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestBoardState {

	private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

	@Test
	public void testBoardState1() throws HSException {
		int numCards = 30;

		Card[] cards1 = new Card[numCards];
		Card[] cards2 = new Card[numCards];
		Card[] cards3 = new Card[numCards];

		for(int i = 0; i < numCards; ++i) {
			byte attack = (byte)((int)(Math.random() * 6) + 1);
			byte health = (byte)((int)(Math.random() * 2) + 1);
			byte mana = (byte)((int)(0.5 * (attack + health)));

			attack = 1;
			health = 4;
			mana = 2;

			cards1[i] = new Minion("" + i, mana, attack, health, attack, health, health);
			cards3[i] = new Minion("" + i, mana, attack, health, attack, health, health);

			attack++;
			cards2[i] = new Minion("" + i, mana, attack, health, attack, health, health);
		}

		BoardModel board1 = new BoardModel();
		BoardModel board2 = new BoardModel();
		BoardModel board3 = new BoardModel();

		assertTrue(board1.equals(board2));

		board1.placeCardHandCurrentPlayer(cards1[0]);
		board3.placeCardHandCurrentPlayer(cards3[0]);

		assertFalse(board1.equals(board2));
		assertTrue(board1.equals(board3));

		board2.placeCardHandCurrentPlayer(cards3[0]);
		assertTrue(board1.equals(board2));

		board1.placeCardHandCurrentPlayer(cards1[1]);
		board3.placeCardHandCurrentPlayer(cards3[1]);
		board2.placeCardHandCurrentPlayer(cards2[1]);
		assertFalse(board1.equals(board2));
		assertTrue(board1.equals(board3));

		board1.placeMinion(PlayerSide.CURRENT_PLAYER, (Minion)cards1[2]);
		board2.placeMinion(PlayerSide.CURRENT_PLAYER, (Minion)cards2[2]);
		board2.placeMinion(PlayerSide.CURRENT_PLAYER, (Minion)cards2[3]);
		board3.placeMinion(PlayerSide.CURRENT_PLAYER, (Minion)cards3[2]);

		assertTrue(board1.equals(board3));
		assertFalse(board1.equals(board2));

		board1.placeMinion(PlayerSide.WAITING_PLAYER, (Minion)cards1[4]);
		board1.placeMinion(PlayerSide.WAITING_PLAYER, (Minion)cards1[5]);
		board3.placeMinion(PlayerSide.WAITING_PLAYER, (Minion)cards3[4]);
		board3.placeMinion(PlayerSide.WAITING_PLAYER, (Minion)cards3[5]);
		assertTrue(board1.equals(board3));

		log.info("board1 hashCode = " + board1.hashCode());
		log.info("board1 hashCode = " + board2.hashCode());
		log.info("board1 hashCode = " + board3.hashCode());
	}

	@Test
	public void testBoardState2() throws HSException {

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

}
