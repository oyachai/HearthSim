package com.hearthsim.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.AbusiveSergeant;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.minion.concrete.ScarletCrusader;
import com.hearthsim.card.minion.heroes.Priest;
import com.hearthsim.card.minion.heroes.Warlock;
import com.hearthsim.card.spellcard.concrete.HolySmite;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestEqualsAndHashCode {

	private static final int nT = 100;
	private static final List<Card> CARD_LIST = Arrays.asList(new HolySmite(), new BloodfenRaptor(), new TheCoin(), new HolySmite());

	private Deck deck0;
	private Deck deck1;
	
	private Deck deckRandom0;
	private Deck deckRandom1;
	
	
	@Before
	public void setup() {

		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}
	
		deck0 = new Deck(cards);
		deck1 = new Deck(cards);
		
		
	}
	
	@Test
	public void test0() {
		
		for (int iter = 0; iter < nT; ++iter) {
			byte mana = (byte)(Math.random() * 10);
			byte attack = (byte)(Math.random() * 10);
			byte health = (byte)(Math.random() * 10);
			Minion minion1 = new Minion("minion1", mana, attack, health, attack, health, health);
			Minion minion2 = new Minion("minion1", mana, attack, health, attack, health, health);
			assertTrue(minion1.equals(minion2));
			assertTrue(minion2.equals(minion1));
			assertEquals(minion1.hashCode(), minion2.hashCode());
		}
	}

	@Test
	public void test1() {
		
		for (int iter = 0; iter < nT; ++iter) {
			byte mana = (byte)(Math.random() * 10);
			byte attack = (byte)(Math.random() * 10);
			byte health = (byte)(Math.random() * 10);
			Minion minion1 = new Minion("minion1", mana, attack, health, attack, health, health);
			Minion minion2 = new Minion("minion2", mana, attack, health, attack, health, health);
			assertFalse(minion1.equals(minion2));
			assertFalse(minion2.equals(minion1));
		}
	}

	@Test
	public void test2() {
		
		for (int iter = 0; iter < nT; ++iter) {
			byte mana = (byte)(Math.random() * 10);
			byte attack1 = (byte)(Math.random() * 10);
			byte attack2 = (byte)(Math.random() * 10);
			byte health = (byte)(Math.random() * 10);
			
			Minion minion1 = new Minion("minion1", mana, attack1, health, attack1, health, health);
			Minion minion2 = new Minion("minion1", mana, attack2, health, attack2, health, health);
			if (attack1 == attack2) {
				assertTrue(minion1.equals(minion2));
				assertTrue(minion2.equals(minion1));
				assertEquals(minion1.hashCode(), minion2.hashCode());
			} else {
				assertFalse(minion1.equals(minion2));
				assertFalse(minion2.equals(minion1));				
			}
		}
	}

	@Test
	public void test3() {
		
		for (int iter = 0; iter < nT; ++iter) {
			byte attack = (byte)(Math.random() * 10);
			byte mana1 = (byte)(Math.random() * 10);
			byte mana2 = (byte)(Math.random() * 10);
			byte health = (byte)(Math.random() * 10);
			
			Minion minion1 = new Minion("minion1", mana1, attack, health, attack, health, health);
			Minion minion2 = new Minion("minion1", mana2, attack, health, attack, health, health);
			if (mana1 == mana2) {
				assertTrue(minion1.equals(minion2));
				assertTrue(minion2.equals(minion1));
				assertEquals(minion1.hashCode(), minion2.hashCode());
			} else {
				assertFalse(minion1.equals(minion2));
				assertFalse(minion2.equals(minion1));				
			}
		}
	}

	@Test
	public void test4() {
		
		for (int iter = 0; iter < nT; ++iter) {
			byte mana = (byte)(Math.random() * 10);
			byte attack = (byte)(Math.random() * 10);
			byte health1 = (byte)(Math.random() * 5);
			byte health2 = (byte)(Math.random() * 5);
			
			Minion minion1 = new Minion("minion1", mana, attack, health1, attack, health1, health1);
			Minion minion2 = new Minion("minion1", mana, attack, health2, attack, health2, health2);
			if (health1 == health2) {
				assertTrue(minion1.equals(minion2));
				assertTrue(minion2.equals(minion1));
				assertEquals(minion1.hashCode(), minion2.hashCode());
			} else {
				assertFalse(minion1.equals(minion2));
				assertFalse(minion2.equals(minion1));				
			}
		}
	}

	
	@Test
	public void testPlayerModel0() {
		
		PlayerModel player0 = new PlayerModel(0, "player0", new Priest(), deck0);
		PlayerModel player1 = new PlayerModel(0, "player0", new Priest(), deck1);
		
		assertTrue(player0.equals(player1));
		assertTrue(player1.equals(player0));
		
		for (int iter = 0; iter < nT; ++iter) {
			byte health0 = (byte)(Math.random() * 10 + 20.0);
			byte health1 = (byte)(Math.random() * 10 + 20.0);
			
			player0.getHero().setHealth(health0);
			player1.getHero().setHealth(health1);

			if (health0 == health1) {
				assertTrue(player0.equals(player1));
				assertTrue(player1.equals(player0));
				assertEquals(player0.hashCode(), player1.hashCode());
			} else {
				assertFalse(player0.equals(player1));
				assertFalse(player1.equals(player0));
			}			
		}
		
		player0.getHero().setHealth((byte)30);
		player1.getHero().setHealth((byte)30);
		for (int iter = 0; iter < nT; ++iter) {
			byte armor0 = (byte)(Math.random() * 10);
			byte armor1 = (byte)(Math.random() * 10);
			
			player0.getHero().setArmor(armor0);
			player1.getHero().setArmor(armor1);

			if (armor0 == armor1) {
				assertTrue(player0.equals(player1));
				assertTrue(player1.equals(player0));
				assertEquals(player0.hashCode(), player1.hashCode());
			} else {
				assertFalse(player0.equals(player1));
				assertFalse(player1.equals(player0));
			}			
		}

	}	

	@Test
	public void testPlayerModel1() {
		
		PlayerModel player0 = new PlayerModel(0, "player0", new Priest(), deck0);
		PlayerModel player1 = new PlayerModel(0, "player0", new Warlock(), deck1);
		
		assertFalse(player0.equals(player1));
		assertFalse(player1.equals(player0));		
	}	

	@Test
	public void testPlayerModel2() {
		
		PlayerModel player0 = new PlayerModel(0, "player0", new Priest(), deck0);
		PlayerModel player1 = new PlayerModel(0, "player1", new Priest(), deck1);
		
		assertFalse(player0.equals(player1));
		assertFalse(player1.equals(player0));		
	}	

	@Test
	public void testPlayerModel3() {
		
		PlayerModel player0 = new PlayerModel(0, "player0", new Priest(), deck0);
		PlayerModel player1 = new PlayerModel(1, "player0", new Priest(), deck1);
		
		assertFalse(player0.equals(player1));
		assertFalse(player1.equals(player0));		
	}	

	@Test
	public void testPlayerModel4() {
		int numCards = 2;
		Card[] cards0 = new Card[numCards];
		Card[] cards1 = new Card[numCards];
		
		
		for (int iter = 0; iter < nT; ++iter) {

			for (int i = 0; i < numCards; ++i) {
				int indx0 = (int)(Math.random() * CARD_LIST.size());
				int indx1 = (int)(Math.random() * CARD_LIST.size());
				cards0[i] = CARD_LIST.get(indx0);
				cards1[i] = CARD_LIST.get(indx1);
			}
			
			deckRandom0 = new Deck(cards0);
			deckRandom1 = new Deck(cards1);
			
			PlayerModel player0 = new PlayerModel(0, "player0", new Priest(), deckRandom0);
			PlayerModel player1 = new PlayerModel(0, "player0", new Priest(), deckRandom1);

			
			boolean deckIsSame = true;
			for(int i = 0; i < numCards; ++i) {
				deckIsSame = deckIsSame && cards0[i].equals(cards1[i]);
			}
			if (deckIsSame) {
				assertTrue(player0.equals(player1));
				assertTrue(player1.equals(player0));
				assertEquals(player0.hashCode(), player1.hashCode());
			} else {
				assertFalse(player0.equals(player1));
				assertFalse(player1.equals(player0));
			}
		}
	}
	
	@Test
	public void testBoardModel0() {

		HearthTreeNode board = new HearthTreeNode(new BoardModel());

		Minion minion0_0 = new BoulderfistOgre();
		Minion minion0_1 = new RaidLeader();
		Minion minion1_0 = new BoulderfistOgre();
		Minion minion1_1 = new RaidLeader();
		Minion minion1_2 = new ScarletCrusader();
		
		board.data_.placeCardHandCurrentPlayer(minion0_0);
		board.data_.placeCardHandCurrentPlayer(minion0_1);
				
		board.data_.placeCardHandWaitingPlayer(minion1_0);
		board.data_.placeCardHandWaitingPlayer(minion1_1);
		board.data_.placeCardHandWaitingPlayer(minion1_2);

		Card fb = new AbusiveSergeant();
		board.data_.placeCardHandCurrentPlayer(fb);

		board.data_.getCurrentPlayer().setMana((byte)8);
		board.data_.getWaitingPlayer().setMana((byte)8);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)8);
		board.data_.getWaitingPlayer().setMaxMana((byte)8);
		
		HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
		try {
			tmpBoard.data_.getCurrentPlayerCardHand(0).getCardAction().useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck1, deck0);
			tmpBoard.data_.getCurrentPlayerCardHand(0).getCardAction().useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck1, deck0);
			tmpBoard.data_.getCurrentPlayerCardHand(0).getCardAction().useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck1, deck0);
		} catch (HSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
		try {
			board.data_.getCurrentPlayerCardHand(0).getCardAction().useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayerHero(), board, deck0, deck1);
			board.data_.getCurrentPlayerCardHand(0).getCardAction().useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayerHero(), board, deck0, deck1);
		} catch (HSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board.data_.resetMana();
		board.data_.resetMinions();
		
		
		assertTrue(board.data_.equals(board.data_));
		assertTrue(board.data_.equals((BoardModel)board.data_.deepCopy()));
		assertEquals(board.data_.hashCode(), ((BoardModel)board.data_.deepCopy()).hashCode());
		assertFalse(board.data_.equals((BoardModel)board.data_.flipPlayers().deepCopy()));
		
	}
}
