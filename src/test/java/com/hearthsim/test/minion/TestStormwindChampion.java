package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.minion.concrete.StormwindChampion;
import com.hearthsim.card.spellcard.concrete.Fireball;
import com.hearthsim.card.spellcard.concrete.HolySmite;
import com.hearthsim.card.spellcard.concrete.Silence;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestStormwindChampion {

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

		board.data_.getCurrentPlayer().setMana((byte)10);
		board.data_.getWaitingPlayer().setMana((byte)10);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)10);
		board.data_.getWaitingPlayer().setMaxMana((byte)10);
		
		HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
		try {
			tmpBoard.data_.getCurrentPlayerCardHand(0).getCardAction().useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);
			tmpBoard.data_.getCurrentPlayerCardHand(0).getCardAction().useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);
		} catch (HSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
		try {
			board.data_.getCurrentPlayerCardHand(0).getCardAction().useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayerHero(), board, deck, null);
			board.data_.getCurrentPlayerCardHand(0).getCardAction().useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayerHero(), board, deck, null);
		} catch (HSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board.data_.resetMana();
		board.data_.resetMinions();
		
		Minion fb = new StormwindChampion();
		board.data_.placeCardHandCurrentPlayer(fb);

	}
	
	@Test
	public void test1() throws HSException {

		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.getCardAction().useOn(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertFalse(ret == null);
		assertTrue(board.data_.getNumCards_hand() == 0);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions() == 3);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions() == 2);
		assertTrue(board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getWaitingPlayerHero().getHealth() == 30);
		
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth() == 6);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth() == 3);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalHealth() == 3);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth() == 2);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth() == 2);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 3);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalAttack(), 5);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 4);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getAuraAttack(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getAuraAttack(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getAuraAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getAuraAttack(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getAuraAttack(), 1);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getAuraHealth(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getAuraHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getAuraHealth(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getAuraHealth(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getAuraHealth(), 0);

	}
	

	@Test
	public void test2() throws HSException {

		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.getCardAction().useOn(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertFalse(ret == null);
		assertTrue(board.data_.getNumCards_hand() == 0);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions() == 3);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions() == 2);
		assertTrue(board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getWaitingPlayerHero().getHealth() == 30);
		
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth() == 6);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth() == 3);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalHealth() == 3);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth() == 2);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth() == 2);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 3);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalAttack(), 5);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 4);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getAuraAttack(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getAuraAttack(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getAuraAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getAuraAttack(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getAuraAttack(), 1);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getAuraHealth(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getAuraHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getAuraHealth(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getAuraHealth(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getAuraHealth(), 0);

		
		
		board.data_.placeCardHandCurrentPlayer(new Fireball());
		theCard = board.data_.getCurrentPlayerCardHand(0);
		target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 1);
		ret = theCard.getCardAction().useOn(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 1);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth() == 6);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth() == 3);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalHealth() == 3);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth() == 2);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 3);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalAttack(), 5);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 3);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getAuraAttack(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getAuraAttack(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getAuraAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getAuraAttack(), 0);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getAuraHealth(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getAuraHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getAuraHealth(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getAuraHealth(), 0);

	}
	

	@Test
	public void test3() throws HSException {

		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.getCardAction().useOn(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertFalse(ret == null);
		assertTrue(board.data_.getNumCards_hand() == 0);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions() == 3);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions() == 2);
		assertTrue(board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getWaitingPlayerHero().getHealth() == 30);

		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth() == 6);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth() == 3);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalHealth() == 3);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth() == 2);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth() == 2);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 3);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalAttack(), 5);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 4);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getAuraAttack(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getAuraAttack(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getAuraAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getAuraAttack(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getAuraAttack(), 1);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getAuraHealth(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getAuraHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getAuraHealth(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getAuraHealth(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getAuraHealth(), 0);

		
		
		board.data_.placeCardHandCurrentPlayer(new Silence());
		theCard = board.data_.getCurrentPlayerCardHand(0);
		target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 1);
		ret = theCard.getCardAction().useOn(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		
		assertFalse(ret == null);
		assertTrue(board.data_.getNumCards_hand() == 0);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions() == 3);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions() == 2);
		assertTrue(board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getWaitingPlayerHero().getHealth() == 30);
		
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth() == 6);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth() == 3);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalHealth() == 3);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth() == 2);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth() == 2);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 3);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalAttack(), 5);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 3);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getAuraAttack(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getAuraAttack(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getAuraAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getAuraAttack(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getAuraAttack(), 0);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getAuraHealth(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getAuraHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getAuraHealth(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getAuraHealth(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getAuraHealth(), 0);
		
		
		board.data_.placeCardHandCurrentPlayer(new Silence());
		theCard = board.data_.getCurrentPlayerCardHand(0);
		target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 1);
		ret = theCard.getCardAction().useOn(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		
		assertFalse(ret == null);
		assertTrue(board.data_.getNumCards_hand() == 0);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions() == 3);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions() == 2);
		assertTrue(board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getWaitingPlayerHero().getHealth() == 30);

		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth() == 6);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth() == 2);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalHealth() == 2);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth() == 2);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth() == 2);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalAttack(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 3);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getAuraAttack(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getAuraAttack(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getAuraAttack(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getAuraAttack(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getAuraAttack(), 0);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getAuraHealth(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getAuraHealth(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getAuraHealth(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getAuraHealth(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getAuraHealth(), 0);
		
		
		
		board.data_.placeCardHandCurrentPlayer(new BloodfenRaptor());
		theCard = board.data_.getCurrentPlayerCardHand(0);
		target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 3);
		ret = theCard.getCardAction().useOn(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		
		assertFalse(ret == null);
		assertTrue(board.data_.getNumCards_hand() == 0);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions() == 4);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions() == 2);
		
		assertTrue(board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getWaitingPlayerHero().getHealth() == 30);
		
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth() == 6);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth() == 2);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalHealth() == 2);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(3).getTotalHealth() == 2);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth() == 2);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth() == 2);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalAttack(), 4);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(3).getTotalAttack(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 3);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getAuraAttack(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getAuraAttack(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getAuraAttack(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(3).getAuraAttack(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getAuraAttack(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getAuraAttack(), 0);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getAuraHealth(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getAuraHealth(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getAuraHealth(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(3).getAuraHealth(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getAuraHealth(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getAuraHealth(), 0);
	}
	
	
	@Test
	public void test4() throws HSException {

		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.getCardAction().useOn(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertFalse(ret == null);
		assertTrue(board.data_.getNumCards_hand() == 0);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions() == 3);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions() == 2);
		assertTrue(board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getWaitingPlayerHero().getHealth() == 30);

		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth() == 6);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth() == 3);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalHealth() == 3);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth() == 2);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth() == 2);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 3);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalAttack(), 5);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 4);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getAuraAttack(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getAuraAttack(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getAuraAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getAuraAttack(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getAuraAttack(), 1);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getAuraHealth(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getAuraHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getAuraHealth(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getAuraHealth(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getAuraHealth(), 0);
		
		board.data_.placeCardHandCurrentPlayer(new HolySmite());
		theCard = board.data_.getCurrentPlayerCardHand(0);
		target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 2);
		ret = theCard.getCardAction().useOn(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		
		assertFalse(ret == null);
		assertTrue(board.data_.getNumCards_hand() == 0);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions() == 3);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions() == 2);
		assertTrue(board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getWaitingPlayerHero().getHealth() == 30);
		
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth() == 6);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth() == 1);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalHealth() == 3);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth() == 2);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth() == 2);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 3);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalAttack(), 5);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 4);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getAuraAttack(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getAuraAttack(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getAuraAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getAuraAttack(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getAuraAttack(), 1);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getAuraHealth(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getAuraHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getAuraHealth(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getAuraHealth(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getAuraHealth(), 0);
		
		
		board.data_.placeCardHandCurrentPlayer(new Silence());
		theCard = board.data_.getCurrentPlayerCardHand(0);
		target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 1);
		ret = theCard.getCardAction().useOn(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		
		assertFalse(ret == null);
		assertTrue(board.data_.getNumCards_hand() == 0);
		assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions() == 3);
		assertTrue(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions() == 2);
		assertTrue(board.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(board.data_.getWaitingPlayerHero().getHealth() == 30);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth(), 6);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalHealth(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth(), 2);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalAttack(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 4);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getAuraAttack(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getAuraAttack(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getAuraAttack(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getAuraAttack(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getAuraAttack(), 1);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getAuraHealth(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getAuraHealth(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getAuraHealth(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getAuraHealth(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getAuraHealth(), 0);
	}
}
