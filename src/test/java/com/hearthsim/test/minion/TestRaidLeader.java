package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.spellcard.concrete.HolySmite;
import com.hearthsim.card.spellcard.concrete.Silence;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestRaidLeader {


	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() {
		board = new HearthTreeNode(new BoardModel());

		Minion minion0_0 = new BloodfenRaptor();
		Minion minion0_1 = new RaidLeader();
		Minion minion1_0 = new BloodfenRaptor();
		Minion minion1_1 = new RaidLeader();
		
		board.data_.placeCardHandCurrentPlayer(minion0_0);
		board.data_.placeCardHandCurrentPlayer(minion0_1);
				
		board.data_.placeCardHandWaitingPlayer(minion1_0);
		board.data_.placeCardHandWaitingPlayer(minion1_1);

		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}
	
		deck = new Deck(cards);

		board.data_.setMana_p0((byte)10);
		board.data_.setMana_p1((byte)10);
		
		board.data_.setMaxMana_p0((byte)10);
		board.data_.setMaxMana_p1((byte)10);
		
		HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
		try {
			tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(tmpBoard.data_.getCurrentPlayer(), tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);
			tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(tmpBoard.data_.getCurrentPlayer(), tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);
		} catch (HSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
		try {
			board.data_.getCurrentPlayerCardHand(0).useOn(board.data_.getCurrentPlayer(), board.data_.getCurrentPlayerHero(), board, deck, null);
			board.data_.getCurrentPlayerCardHand(0).useOn(board.data_.getCurrentPlayer(), board.data_.getCurrentPlayerHero(), board, deck, null);
		} catch (HSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board.data_.resetMana();
		board.data_.resetMinions();
		
		Minion fb = new RaidLeader();
		board.data_.placeCardHandCurrentPlayer(fb);

	}
	
	@Test
	public void test1() throws HSException {

		Minion target = board.data_.getCharacter(board.data_.getCurrentPlayer(), 0);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(board.data_.getCurrentPlayer(), target, board, deck, null);
		assertFalse(ret == null);
		assertTrue(board.data_.getNumCards_hand() == 0);
		assertTrue(board.data_.getCurrentPlayer().getNumMinions() == 3);
		assertTrue(board.data_.getWaitingPlayer().getNumMinions() == 2);
		assertTrue(board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getWaitingPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(0).getHealth() == 2);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(1).getHealth() == 2);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(2).getHealth() == 2);
		assertTrue(board.data_.getWaitingPlayer().getMinions().get(0).getHealth() == 2);
		assertTrue(board.data_.getWaitingPlayer().getMinions().get(1).getHealth() == 2);
		
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 3);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 3);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 5);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 4);
		
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getAuraAttack(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getAuraAttack(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(2).getAuraAttack(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getAuraAttack(), 0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getAuraAttack(), 1);

	}
	

	@Test
	public void test2() throws HSException {

		Minion target = board.data_.getCharacter(board.data_.getCurrentPlayer(), 0);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(board.data_.getCurrentPlayer(), target, board, deck, null);
		assertFalse(ret == null);
		assertTrue(board.data_.getNumCards_hand() == 0);
		assertTrue(board.data_.getCurrentPlayer().getNumMinions() == 3);
		assertTrue(board.data_.getWaitingPlayer().getNumMinions() == 2);
		assertTrue(board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getWaitingPlayerHero().getHealth() == 30);
		
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(0).getHealth() == 2);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(1).getHealth() == 2);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(2).getHealth() == 2);
		assertTrue(board.data_.getWaitingPlayer().getMinions().get(0).getHealth() == 2);
		assertTrue(board.data_.getWaitingPlayer().getMinions().get(1).getHealth() == 2);
		
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 3);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 3);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 5);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 4);
		
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getAuraAttack(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getAuraAttack(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(2).getAuraAttack(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getAuraAttack(), 0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getAuraAttack(), 1);

		
		
		board.data_.placeCardHandCurrentPlayer(new HolySmite());
		theCard = board.data_.getCurrentPlayerCardHand(0);
		target = board.data_.getCharacter(board.data_.getWaitingPlayer(), 1);
		ret = theCard.useOn(board.data_.getWaitingPlayer(), target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getCurrentPlayer().getNumMinions(), 3);
		assertEquals(board.data_.getWaitingPlayer().getNumMinions(), 1);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getHealth(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getHealth(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(2).getHealth(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getHealth(), 2);
		
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 3);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 3);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 5);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 3);
		
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getAuraAttack(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getAuraAttack(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(2).getAuraAttack(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getAuraAttack(), 0);
	}
	

	@Test
	public void test3() throws HSException {

		Minion target = board.data_.getCharacter(board.data_.getCurrentPlayer(), 0);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(board.data_.getCurrentPlayer(), target, board, deck, null);
		assertFalse(ret == null);
		assertTrue(board.data_.getNumCards_hand() == 0);
		assertTrue(board.data_.getCurrentPlayer().getNumMinions() == 3);
		assertTrue(board.data_.getWaitingPlayer().getNumMinions() == 2);
		assertTrue(board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getWaitingPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(0).getHealth() == 2);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(1).getHealth() == 2);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(2).getHealth() == 2);
		assertTrue(board.data_.getWaitingPlayer().getMinions().get(0).getHealth() == 2);
		assertTrue(board.data_.getWaitingPlayer().getMinions().get(1).getHealth() == 2);
		
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 3);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 3);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 5);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 4);
		
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getAuraAttack(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getAuraAttack(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(2).getAuraAttack(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getAuraAttack(), 0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getAuraAttack(), 1);

		
		
		board.data_.placeCardHandCurrentPlayer(new Silence());
		theCard = board.data_.getCurrentPlayerCardHand(0);
		target = board.data_.getCharacter(board.data_.getWaitingPlayer(), 1);
		ret = theCard.useOn(board.data_.getWaitingPlayer(), target, board, deck, null);
		
		assertFalse(ret == null);
		assertTrue(board.data_.getNumCards_hand() == 0);
		assertTrue(board.data_.getCurrentPlayer().getNumMinions() == 3);
		assertTrue(board.data_.getWaitingPlayer().getNumMinions() == 2);
		assertTrue(board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getWaitingPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(0).getHealth() == 2);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(1).getHealth() == 2);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(2).getHealth() == 2);
		assertTrue(board.data_.getWaitingPlayer().getMinions().get(0).getHealth() == 2);
		assertTrue(board.data_.getWaitingPlayer().getMinions().get(1).getHealth() == 2);
		
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 3);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 3);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 5);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 3);
		
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getAuraAttack(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getAuraAttack(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(2).getAuraAttack(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getAuraAttack(), 0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getAuraAttack(), 0);
		
		board.data_.placeCardHandCurrentPlayer(new Silence());
		theCard = board.data_.getCurrentPlayerCardHand(0);
		target = board.data_.getCharacter(board.data_.getCurrentPlayer(), 1);
		ret = theCard.useOn(board.data_.getCurrentPlayer(), target, board, deck, null);
		
		assertFalse(ret == null);
		assertTrue(board.data_.getNumCards_hand() == 0);
		assertTrue(board.data_.getCurrentPlayer().getNumMinions() == 3);
		assertTrue(board.data_.getWaitingPlayer().getNumMinions() == 2);
		assertTrue(board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getWaitingPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(0).getHealth() == 2);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(1).getHealth() == 2);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(2).getHealth() == 2);
		assertTrue(board.data_.getWaitingPlayer().getMinions().get(0).getHealth() == 2);
		assertTrue(board.data_.getWaitingPlayer().getMinions().get(1).getHealth() == 2);
		
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 3);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 4);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 3);
		
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getAuraAttack(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getAuraAttack(), 0);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(2).getAuraAttack(), 1);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getAuraAttack(), 0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getAuraAttack(), 0);
		
		board.data_.placeCardHandCurrentPlayer(new BloodfenRaptor());
		theCard = board.data_.getCurrentPlayerCardHand(0);
		target = board.data_.getCharacter(board.data_.getCurrentPlayer(), 3);
		ret = theCard.useOn(board.data_.getCurrentPlayer(), target, board, deck, null);
		
		assertFalse(ret == null);
		assertTrue(board.data_.getNumCards_hand() == 0);
		assertTrue(board.data_.getCurrentPlayer().getNumMinions() == 4);
		assertTrue(board.data_.getWaitingPlayer().getNumMinions() == 2);
		assertTrue(board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getWaitingPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(0).getHealth() == 2);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(1).getHealth() == 2);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(2).getHealth() == 2);
		assertTrue(board.data_.getCurrentPlayer().getMinions().get(3).getHealth() == 2);
		assertTrue(board.data_.getWaitingPlayer().getMinions().get(0).getHealth() == 2);
		assertTrue(board.data_.getWaitingPlayer().getMinions().get(1).getHealth() == 2);
		
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 3);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 4);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(3).getTotalAttack(), 4);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 3);
		
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getAuraAttack(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getAuraAttack(), 0);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(2).getAuraAttack(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(3).getAuraAttack(), 1);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getAuraAttack(), 0);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getAuraAttack(), 0);
	}
}
