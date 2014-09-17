package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.AcolyteOfPain;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.GoldshireFootman;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.*;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestAcolyteOfPain {
	
	private HearthTreeNode board;
	private Deck deck;


	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel());

		Minion minion0_0 = new AcolyteOfPain();
		Minion minion0_1 = new GoldshireFootman();
		Minion minion1_0 = new GoldshireFootman();
		Minion minion1_1 = new GoldshireFootman();
		
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_1);
		
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_1);
		
		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new BloodfenRaptor();
		}
	
		deck = new Deck(cards);

		Minion fb = new AcolyteOfPain();
		board.data_.placeCardHandCurrentPlayer(fb);

		board.data_.setMana_p0((byte)7);
		board.data_.setMana_p1((byte)7);
		
		board.data_.setMaxMana_p0((byte)7);
		board.data_.setMaxMana_p1((byte)7);
		
	}
	
	@Test
	public void test0() throws HSException {
		
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		
		assertTrue(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 3);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 2);
	}
	

	@Test
	public void test2() throws HSException {
		
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 2);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(board.data_.getMana_p0(), 4);
		assertEquals(board.data_.getMana_p1(), 7);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 3);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getHealth(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalAttack(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 1);
	}
	
	@Test
	public void test3() throws HSException {
		
		

		
		Hero hero = new Hero();
		PlayerModel playerModel0 = new PlayerModel("player0", hero, deck);
		PlayerModel playerModel1 = new PlayerModel("player0", hero, deck);
		
		board.data_.setMana_p0((byte)1);
		board.data_.setMana_p1((byte)1);
		
		board.data_.setMaxMana_p0((byte)1);
		board.data_.setMaxMana_p1((byte)1);


        ArtificialPlayer ai0 = ArtificialPlayer.buildStandardAI1();
		BoardModel resBoard = ai0.playTurn(0, board.data_, playerModel0, playerModel1);
		
		assertEquals(resBoard.getNumCardsHandCurrentPlayer(), 2); //1 card drawn from AcolyteOfPain, not enough mana to play it
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getNumMinions(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(resBoard).getNumMinions(), 1); //1 minion should have been killed
		assertEquals(resBoard.getMana_p0(), 1); //0 mana used
		assertEquals(resBoard.getMana_p1(), 1);
		assertEquals(resBoard.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(resBoard.getWaitingPlayerHero().getHealth(), 30);
	}
	
	@Test
	public void test4() throws HSException {
		

		
		Hero hero = new Hero();
		PlayerModel playerModel0 = new PlayerModel("player0", hero, deck);
		PlayerModel playerModel1 = new PlayerModel("player0", hero, deck);
		
		board.data_.setMana_p0((byte)3);
		board.data_.setMana_p1((byte)3);
		
		board.data_.setMaxMana_p0((byte)3);
		board.data_.setMaxMana_p1((byte)3);


        ArtificialPlayer ai0 = ArtificialPlayer.buildStandardAI1();
		BoardModel resBoard = ai0.playTurn(0, board.data_, playerModel0, playerModel1, 200000000);
		
		assertEquals(resBoard.getNumCardsHandCurrentPlayer(), 1); //1 card drawn from AcolyteOfPain, then played the Bloodfen Raptor
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getNumMinions(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(resBoard).getNumMinions(), 1); //1 minion should have been killed
		assertEquals(resBoard.getMana_p0(), 1); //2 mana used... it's better to put down a Bloodfen Raptor than an Acolyte of Pain
		assertEquals(resBoard.getMana_p1(), 3);
		assertEquals(resBoard.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(resBoard.getWaitingPlayerHero().getHealth(), 30);
	}
	
	@Test
	public void test5() throws HSException {
		

		
		Hero hero = new Hero();
		PlayerModel playerModel0 = new PlayerModel("player0", hero, deck);
		PlayerModel playerModel1 = new PlayerModel("player0", hero, deck);
		
		board.data_.setMana_p0((byte)3);
		board.data_.setMana_p1((byte)3);
		
		board.data_.setMaxMana_p0((byte)3);
		board.data_.setMaxMana_p1((byte)3);

		board.data_.removeMinion(PlayerSide.WAITING_PLAYER, 0);
		board.data_.removeMinion(PlayerSide.WAITING_PLAYER, 0);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new AcolyteOfPain());
		
		assertEquals(board.data_.getNumCardsHandWaitingPlayer(), 0);

        ArtificialPlayer ai0 = ArtificialPlayer.buildStandardAI1();
		BoardModel resBoard = ai0.playTurn(0, board.data_, playerModel0, playerModel1);
		
		assertEquals(resBoard.getNumCardsHandCurrentPlayer(), 1); //1 card drawn from AcolyteOfPain, then played the Bloodfen Raptor
		assertEquals(resBoard.getNumCardsHandWaitingPlayer(), 1); //1 card drawn from AcolyteOfPain.  The Acolytes smack into each other.
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getNumMinions(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(resBoard).getNumMinions(), 1); //1 minion should have been killed
		assertEquals(resBoard.getMana_p0(), 1); //2 mana used... it's better to put down a Bloodfen Raptor than an Acolyte of Pain
		assertEquals(resBoard.getMana_p1(), 3);
		assertEquals(resBoard.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(resBoard.getWaitingPlayerHero().getHealth(), 29);
	}
}
