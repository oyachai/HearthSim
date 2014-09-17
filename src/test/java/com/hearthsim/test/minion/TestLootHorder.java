package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.LootHoarder;
import com.hearthsim.card.minion.concrete.RiverCrocolisk;
import com.hearthsim.card.minion.concrete.Wisp;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.*;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestLootHorder {
	
	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel());

		Minion minion0_0 = new LootHoarder();
		Minion minion1_0 = new BloodfenRaptor();
		
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);
		
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);
		
		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new RiverCrocolisk();
		}
	
		deck = new Deck(cards);
		
	}
	
	
	@Test
	public void test0() throws HSException {


        ArtificialPlayer ai0 = ArtificialPlayer.buildStandardAI1();

        Hero hero = new Hero();
		PlayerModel playerModel0 = new PlayerModel("player0", hero, deck);
		PlayerModel playerModel1 = new PlayerModel("player0", hero, deck);
		
		board.data_.setMana_p0((byte)1);
		board.data_.setMana_p1((byte)1);
		
		board.data_.setMaxMana_p0((byte)1);
		board.data_.setMaxMana_p1((byte)1);


		BoardModel resBoard = ai0.playTurn(0, board.data_, playerModel0, playerModel1);
		
		assertEquals(resBoard.getNumCardsHandCurrentPlayer(), 1); //1 card drawn from Loot Horder attacking and dying, no mana left to play the card
		assertEquals(resBoard.getCurrentPlayer().getNumMinions(), 0);
		assertEquals(resBoard.getWaitingPlayer().getNumMinions(), 0); //1 minion should have been killed
		assertEquals(resBoard.getMana_p0(), 1); //no mana used
		assertEquals(resBoard.getMana_p1(), 1);
		assertEquals(resBoard.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(resBoard.getWaitingPlayerHero().getHealth(), 30);
	}
	

	@Test
	public void test1() throws HSException {

        ArtificialPlayer ai0 = ArtificialPlayer.buildStandardAI1();

		Hero hero = new Hero();
		PlayerModel playerModel0 = new PlayerModel("player0", hero, deck);
		PlayerModel playerModel1 = new PlayerModel("player0", hero, deck);
		
		board.data_.setMana_p0((byte)3);
		board.data_.setMana_p1((byte)3);
		
		board.data_.setMaxMana_p0((byte)3);
		board.data_.setMaxMana_p1((byte)3);


		BoardModel resBoard = ai0.playTurn(0, board.data_, playerModel0, playerModel1);
		
		assertEquals(resBoard.getNumCardsHandCurrentPlayer(), 0); //1 card drawn from Loot Horder attacking and dying, then played the drawn card
		assertEquals(resBoard.getCurrentPlayer().getNumMinions(), 1);
		assertEquals(resBoard.getWaitingPlayer().getNumMinions(), 0); //1 minion should have been killed
		assertEquals(resBoard.getMana_p0(), 1); //2 mana used
		assertEquals(resBoard.getMana_p1(), 3);
		assertEquals(resBoard.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(resBoard.getWaitingPlayerHero().getHealth(), 30);
	}
	
	@Test
	public void test2() throws HSException {

        ArtificialPlayer ai0 = ArtificialPlayer.buildStandardAI1();
		
		//remove the loot hoarder from player0, add a Wisp
		board.data_.removeMinion(PlayerSide.CURRENT_PLAYER, 0);
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new Wisp());
		
		//remove the Bloodfen Raptor from player0, add a Loot Hoarder
		board.data_.removeMinion(PlayerSide.WAITING_PLAYER, 0);
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new LootHoarder());

		Hero hero = new Hero();
		PlayerModel playerModel0 = new PlayerModel("player0", hero, deck);
		PlayerModel playerModel1 = new PlayerModel("player0", hero, deck);
		
		board.data_.setMana_p0((byte)3);
		board.data_.setMana_p1((byte)3);
		
		board.data_.setMaxMana_p0((byte)3);
		board.data_.setMaxMana_p1((byte)3);


		BoardModel resBoard = ai0.playTurn(0, board.data_, playerModel0, playerModel1);
		
		assertEquals(resBoard.getNumCardsHandCurrentPlayer(), 0); //no cards in hand
		assertEquals(resBoard.getNumCardsHandWaitingPlayer(), 1); //drew cards from the loot horder that was killed
		assertEquals(resBoard.getCurrentPlayer().getNumMinions(), 0);
		assertEquals(resBoard.getWaitingPlayer().getNumMinions(), 0); //1 minion should have been killed
		assertEquals(resBoard.getMana_p0(), 3); //no mana used
		assertEquals(resBoard.getMana_p1(), 3);
		assertEquals(resBoard.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(resBoard.getWaitingPlayerHero().getHealth(), 30);
	}
}
