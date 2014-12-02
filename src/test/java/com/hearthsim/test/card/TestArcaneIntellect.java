package com.hearthsim.test.card;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.spellcard.concrete.ArcaneIntellect;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestArcaneIntellect {

	private HearthTreeNode board;
	private static final byte mana = 2;
	private static final byte attack0 = 2;
	private static final byte health0 = 5;
	private static final byte health1 = 1;

	@Before
	public void setup() throws HSException {
		
		int numCards = 10;
		Card cards[] = new Card[numCards];
		for (int index = 0; index < numCards; ++index) {
			cards[index] = new BloodfenRaptor();
		}
		
		Deck deck = new Deck(cards);
		PlayerModel playerModel0 = new PlayerModel(0, "player0", new Hero(), deck);
		PlayerModel playerModel1 = new PlayerModel(1, "player1", new Hero(), deck);

		board = new HearthTreeNode(new BoardModel(playerModel0, playerModel1));
		
		Minion minion0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion1 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		Minion minion2 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1);
		Minion minion3 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);

		ArcaneIntellect fb = new ArcaneIntellect();
		board.data_.placeCardHandCurrentPlayer(fb);
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion2);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion3);
		
		board.data_.getCurrentPlayer().setMana(5);
	}
	
	@Test
	public void test0() throws HSException {

		
		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}
		
		Deck deck = new Deck(cards);
		
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode res;
		Minion target = null;
		
		target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		res = theCard.getCardAction().useOn(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 1);
		res = theCard.getCardAction().useOn(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 1);
		res = theCard.getCardAction().useOn(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 2);
		res = theCard.getCardAction().useOn(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 3);
		res = theCard.getCardAction().useOn(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		res = theCard.getCardAction().useOn(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertFalse(res == null);
		assertEquals(res.data_.getNumCards_hand(), 0);
		assertTrue(res instanceof CardDrawNode);
		assertEquals( ((CardDrawNode)res).getNumCardsToDraw(), 2);
		
		assertTrue(res.data_.getCurrentPlayer().getNumMinions() == 1);
		assertTrue(res.data_.getWaitingPlayer().getNumMinions() == 3);
		assertTrue(res.data_.getCurrentPlayer().getMana() == 2);
		assertTrue(res.data_.getCurrentPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue(res.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack() == attack0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack() == attack0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(1).getHealth() == health1);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack() == attack0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(2).getHealth() == health0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(2).getTotalAttack() == attack0);
		assertTrue(res.data_.getCurrentPlayerHero().getHealth() == 30);
		assertTrue(res.data_.getWaitingPlayerHero().getHealth() == 30);

	}

	@Test
	public void test1() throws HSException {
		
		Card cards[] = new Card[1];
		for (int index = 0; index < 1; ++index) {
			cards[index] = new TheCoin();
		}
		
		Deck deck = new Deck(cards);
		
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode res;
		Minion target = null;
		
		target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		res = theCard.getCardAction().useOn(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 1);
		res = theCard.getCardAction().useOn(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 1);
		res = theCard.getCardAction().useOn(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 2);
		res = theCard.getCardAction().useOn(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 3);
		res = theCard.getCardAction().useOn(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertTrue(res == null);
		
		target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		res = theCard.getCardAction().useOn(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertFalse(res == null);
		assertEquals(res.data_.getNumCards_hand(), 0);
		assertTrue(res instanceof CardDrawNode);
		assertEquals( ((CardDrawNode)res).getNumCardsToDraw(), 2);

		assertTrue(res.data_.getCurrentPlayer().getNumMinions() == 1);
		assertTrue(res.data_.getWaitingPlayer().getNumMinions() == 3);
		assertTrue(res.data_.getCurrentPlayer().getMana() == 2);
		assertTrue(res.data_.getCurrentPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue(res.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack() == attack0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(0).getHealth() == health0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack() == attack0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(1).getHealth() == health1);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack() == attack0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(2).getHealth() == health0);
		assertTrue(res.data_.getWaitingPlayer().getMinions().get(2).getTotalAttack() == attack0);

	}

	@Test
	public void test2() throws HSException {
		
		int numCards = 10;
		Card cards[] = new Card[numCards];
		for (int index = 0; index < numCards; ++index) {
			cards[index] = new BloodfenRaptor();
		}
		
		board.data_.getCurrentPlayer().setMana((byte)3);
		board.data_.getWaitingPlayer().setMana((byte)3);

		board.data_.getCurrentPlayer().setMaxMana((byte)3);
		board.data_.getWaitingPlayer().setMaxMana((byte)3);

        ArtificialPlayer ai0 = ArtificialPlayer.buildStandardAI1();
		BoardModel resBoard = ai0.playTurn(0, board.data_, 20000000);
		
		assertFalse( resBoard == null );
		
		assertEquals( resBoard.getCurrentPlayer().getMana(), 0 );
		assertEquals( resBoard.getWaitingPlayer().getMana(), 3 );
		assertEquals( resBoard.getNumCardsHandCurrentPlayer(), 2 );
		assertEquals( PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getNumMinions(), 1 );
		assertEquals( PlayerSide.WAITING_PLAYER.getPlayer(resBoard).getNumMinions(), 2 );
	}
	
	@Test
	public void test3() throws HSException {
		
		int numCards = 10;
		Card cards[] = new Card[numCards];
		for (int index = 0; index < numCards; ++index) {
			cards[index] = new BloodfenRaptor();
		}

		board.data_.getCurrentPlayer().setMana((byte)6);
		board.data_.getWaitingPlayer().setMana((byte)6);

		board.data_.getCurrentPlayer().setMaxMana((byte)6);
		board.data_.getWaitingPlayer().setMaxMana((byte)6);

        ArtificialPlayer ai0 = ArtificialPlayer.buildStandardAI1();
		BoardModel resBoard = ai0.playTurn(0, board.data_);
		
		assertFalse( resBoard == null );
		
		assertEquals( resBoard.getCurrentPlayer().getMana(), 1 );
		assertEquals( resBoard.getWaitingPlayer().getMana(), 6 );
		assertEquals( resBoard.getNumCardsHandCurrentPlayer(), 1 );
		assertEquals( PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getNumMinions(), 2 );
		assertEquals( PlayerSide.WAITING_PLAYER.getPlayer(resBoard).getNumMinions(), 2 );
	}
	
	@Test
	public void test4() throws HSException {
		
		int numCards = 10;
		Card cards[] = new Card[numCards];
		for (int index = 0; index < numCards; ++index) {
			cards[index] = new BloodfenRaptor();
		}

		board.data_.getCurrentPlayer().setMana((byte)9);
		board.data_.getWaitingPlayer().setMana((byte)9);

		board.data_.getCurrentPlayer().setMaxMana((byte)9);
		board.data_.getWaitingPlayer().setMaxMana((byte)9);

        ArtificialPlayer ai0 = ArtificialPlayer.buildStandardAI1();
		BoardModel resBoard = ai0.playTurn(0, board.data_);
		
		assertFalse( resBoard == null );
		
		assertEquals( resBoard.getCurrentPlayer().getMana(), 2 );
		assertEquals( resBoard.getWaitingPlayer().getMana(), 9 );
		assertEquals( resBoard.getNumCardsHandCurrentPlayer(), 0 );
		assertEquals( PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getNumMinions(), 3 );
		assertEquals( PlayerSide.WAITING_PLAYER.getPlayer(resBoard).getNumMinions(), 2 );
	}
}
