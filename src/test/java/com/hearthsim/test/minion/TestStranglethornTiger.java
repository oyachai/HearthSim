package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.StranglethornTiger;
import com.hearthsim.card.spellcard.concrete.Silence;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestStranglethornTiger {


	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() {
		board = new HearthTreeNode(new BoardModel());

		Minion minion0_0 = new BoulderfistOgre();
		Minion minion0_1 = new BoulderfistOgre();
		Minion minion1_1 = new StranglethornTiger();
		
		board.data_.placeCardHandCurrentPlayer(minion0_0);
		board.data_.placeCardHandCurrentPlayer(minion0_1);
				
		board.data_.placeCardHandWaitingPlayer(minion1_1);

		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}
	
		deck = new Deck(cards);

		board.data_.getCurrentPlayer().setMana((byte)8);
		board.data_.getWaitingPlayer().setMana((byte)8);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)8);
		board.data_.getWaitingPlayer().setMaxMana((byte)8);
		
		HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
		try {
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
		
	}
	
	

	@Test
	public void test0() throws HSException {
		
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 1);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 8);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 8);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth(), 7);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth(), 5);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 6);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 6);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 5);
	}
	
	
	@Test
	public void test1() throws HSException {
		
		//In this test, the Stranglethorn Tiger is stealthed, so player0 has no choice but to hit the enemy hero for 12 damage

        ArtificialPlayer ai0 = ArtificialPlayer.buildStandardAI1();
		BoardModel resBoard = ai0.playTurn(0, board.data_);

		assertEquals(resBoard.getNumCardsHandCurrentPlayer(), 0);
		assertEquals(resBoard.getNumCardsHandWaitingPlayer(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getNumMinions(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(resBoard).getNumMinions(), 1);
		assertEquals(resBoard.getCurrentPlayer().getMana(), 8);
		assertEquals(resBoard.getWaitingPlayer().getMana(), 8);
		assertEquals(resBoard.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(resBoard.getWaitingPlayerHero().getHealth(), 18);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getMinions().get(0).getTotalHealth(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getMinions().get(1).getTotalHealth(), 7);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(resBoard).getMinions().get(0).getTotalHealth(), 5);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getMinions().get(0).getTotalAttack(), 6);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getMinions().get(1).getTotalAttack(), 6);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(resBoard).getMinions().get(0).getTotalAttack(), 5);
	}
	
	@Test
	public void test2() throws HSException {
		
		//In this test, player0 is given a Silence.  It can't use it though because it can't target the stealthed Stranglethorn Tiger.

		board.data_.placeCardHandCurrentPlayer(new Silence());

        ArtificialPlayer ai0 = ArtificialPlayer.buildStandardAI1();
		BoardModel resBoard = ai0.playTurn(0, board.data_);

		assertEquals(resBoard.getNumCardsHandCurrentPlayer(), 1);
		assertEquals(resBoard.getNumCardsHandWaitingPlayer(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getNumMinions(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(resBoard).getNumMinions(), 1);
		assertEquals(resBoard.getCurrentPlayer().getMana(), 8);
		assertEquals(resBoard.getWaitingPlayer().getMana(), 8);
		assertEquals(resBoard.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(resBoard.getWaitingPlayerHero().getHealth(), 18);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getMinions().get(0).getTotalHealth(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getMinions().get(1).getTotalHealth(), 7);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(resBoard).getMinions().get(0).getTotalHealth(), 5);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getMinions().get(0).getTotalAttack(), 6);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getMinions().get(1).getTotalAttack(), 6);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(resBoard).getMinions().get(0).getTotalAttack(), 5);
	}
	
	@Test
	public void test3() throws HSException {

		//In this test, player1 goes first.  It uses the Stranglethorn Tiger to attack the hero, which removes stealth from 
		// the tiger.  Then, player0 plays a turn in which it is able to kill the tiger and hit the player1's hero for 6.  

        ArtificialPlayer ai0 = ArtificialPlayer.buildStandardAI1();
		BoardModel resBoard0 = ai0.playTurn(0, board.data_.flipPlayers(), 2000000000);
		BoardModel resBoard1 = ai0.playTurn(0, resBoard0.flipPlayers(), 2000000000);

		assertEquals(resBoard1.getNumCardsHandWaitingPlayer(), 0);
		assertEquals(resBoard1.getNumCardsHandWaitingPlayer(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard1).getNumMinions(), 2);
		assertEquals(resBoard1.getWaitingPlayer().getNumMinions(), 0);
		assertEquals(resBoard1.getCurrentPlayer().getMana(), 8);
		assertEquals(resBoard1.getWaitingPlayer().getMana(), 8);
		assertEquals(resBoard1.getCurrentPlayerHero().getHealth(), 25);
		assertEquals(resBoard1.getWaitingPlayerHero().getHealth(), 24);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard1).getMinions().get(0).getTotalHealth(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard1).getMinions().get(1).getTotalHealth(), 2);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard1).getMinions().get(0).getTotalAttack(), 6);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard1).getMinions().get(1).getTotalAttack(), 6);
	}
}
