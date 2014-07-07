package com.hearthsim.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.util.BoardState;

public class testBoardState {

	@Test
	public void testBoardState1() {
		int numCards = 30;
		
		Card[] cards1 = new Card[numCards];
		Card[] cards2 = new Card[numCards];
		Card[] cards3 = new Card[numCards];

		for (int i = 0; i < numCards; ++i) {
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
		
		Hero hero1 = new Hero();
		Hero hero2 = new Hero();

		BoardState board1 = new BoardState();
		BoardState board2 = new BoardState();
		BoardState board3 = new BoardState();
		
		assertTrue(board1.equals(board2));
		
		board1.placeCard_hand_p0(cards1[0]);
		board3.placeCard_hand_p0(cards3[0]);

		assertFalse(board1.equals(board2));
		assertTrue(board1.equals(board3));
		
		board2.placeCard_hand_p0(cards3[0]);
		assertTrue(board1.equals(board2));
		
		board1.placeCard_hand_p0(cards1[1]);
		board3.placeCard_hand_p0(cards3[1]);
		board2.placeCard_hand_p0(cards2[1]);
		assertFalse(board1.equals(board2));
		assertTrue(board1.equals(board3));
		
		board1.placeMinion_p0((Minion)cards1[2]);
		board2.placeMinion_p0((Minion)cards2[2]);
		board2.placeMinion_p0((Minion)cards2[3]);
		board3.placeMinion_p0((Minion)cards3[2]);


		assertTrue(board1.equals(board3));
		assertFalse(board1.equals(board2));

		board1.placeMinion_p1((Minion)cards1[4]);
		board1.placeMinion_p1((Minion)cards1[5]);
		board3.placeMinion_p1((Minion)cards3[4]);
		board3.placeMinion_p1((Minion)cards3[5]);
		assertTrue(board1.equals(board3));

		
		System.out.println("board1 hashCode = " + board1.hashCode());
		System.out.println("board1 hashCode = " + board2.hashCode());
		System.out.println("board1 hashCode = " + board3.hashCode());
	}

	@Test
	public void testBoardState2() {

		int numBoards = 20000;
		BoardState[] boards = new BoardState[numBoards];

		int numCards1 = 1;
		int numCards2 = 1;
		int numCards3 = 2;
		
		Card[] cards1 = new Card[numCards1];
		Card[] cards2 = new Card[numCards2];
		Card[] cards3 = new Card[numCards3];

		for (int i = 0; i < numCards1; ++i) {
			byte attack = (byte)((int)(Math.random() * 6) + 1);
			byte health = (byte)((int)(Math.random() * 2) + 1);
			byte mana = (byte)((int)(0.5 * (attack + health)));
			
			cards1[i] = new Minion("" + i, mana, attack, health, attack, health, health);
		}

		for (int i = 0; i < numCards2; ++i) {
			byte attack = (byte)((int)(Math.random() * 6) + 1);
			byte health = (byte)((int)(Math.random() * 2) + 1);
			byte mana = (byte)((int)(0.5 * (attack + health)));
			
			cards2[i] = new Minion("" + i, mana, attack, health, attack, health, health);
		}

		for (int i = 0; i < numCards3; ++i) {
			byte attack = (byte)((int)(Math.random() * 6) + 1);
			byte health = (byte)((int)(Math.random() * 2) + 1);
			byte mana = (byte)((int)(0.5 * (attack + health)));
			
			cards3[i] = new Minion("" + i, mana, attack, health, attack, health, health);
		}

		for (int i = 0; i < numBoards; ++i) {
			boards[i] = new BoardState();
			
			int nh = (int)(Math.random() * 1) + 1;
			int nm1 = (int)(Math.random() * 1) + 1;
			int nm2 = (int)(Math.random() * 2) + 1;
			
			for (int j = 0; j < nh; ++j) {
				boards[i].placeCard_hand_p0(cards1[(int)(Math.random() * numCards1)]);
			}

			for (int j = 0; j < nm1; ++j) {
				boards[i].placeMinion_p0((Minion)cards2[(int)(Math.random() * numCards2)]);
			}

			for (int j = 0; j < nm2; ++j) {
				boards[i].placeMinion_p1((Minion)cards3[(int)(Math.random() * numCards3)]);
			}
		}
		
		long nT = 0;
		long nA = 0;
		for (int i = 0; i < numBoards; ++i) {
			for (int j = i+1; j < numBoards; ++j) {
				boolean res = boards[i].equals(boards[j]);
				if (res) nT = nT + 1;
				nA = nA + 1;
			}
		}
		System.out.println("t frac = " + (double)nT / (double)nA);
	}
	
}
