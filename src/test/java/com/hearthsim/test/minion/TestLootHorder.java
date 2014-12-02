package com.hearthsim.test.minion;

import com.hearthsim.card.Card;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.LootHoarder;
import com.hearthsim.card.minion.concrete.RiverCrocolisk;
import com.hearthsim.card.minion.concrete.Wisp;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
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
		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new RiverCrocolisk();
		}
	
		deck = new Deck(cards);
		PlayerModel playerModel0 = new PlayerModel(0, "player0", new Hero(), deck);
		PlayerModel playerModel1 = new PlayerModel(1, "player1", new Hero(), deck);

		board = new HearthTreeNode(new BoardModel(playerModel0, playerModel1));

		Minion minion0_0 = new LootHoarder();
		Minion minion1_0 = new BloodfenRaptor();
		
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);
		
		board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);

	}
	
	
	@Test
	public void test0() throws HSException {


        ArtificialPlayer ai0 = ArtificialPlayer.buildStandardAI1();
		
		board.data_.getCurrentPlayer().setMana((byte)1);
		board.data_.getWaitingPlayer().setMana((byte)1);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)1);
		board.data_.getWaitingPlayer().setMaxMana((byte)1);


		BoardModel resBoard = ai0.playTurn(0, board.data_);
		
		assertEquals(resBoard.getNumCardsHandCurrentPlayer(), 1); //1 card drawn from Loot Horder attacking and dying, no mana left to play the card
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getNumMinions(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(resBoard).getNumMinions(), 0); //1 minion should have been killed
		assertEquals(resBoard.getCurrentPlayer().getMana(), 1); //no mana used
		assertEquals(resBoard.getWaitingPlayer().getMana(), 1);
		assertEquals(resBoard.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(resBoard.getWaitingPlayerHero().getHealth(), 30);
	}
	

	@Test
	public void test1() throws HSException {

        ArtificialPlayer ai0 = ArtificialPlayer.buildStandardAI1();

		board.data_.getCurrentPlayer().setMana((byte)3);
		board.data_.getWaitingPlayer().setMana((byte)3);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)3);
		board.data_.getWaitingPlayer().setMaxMana((byte)3);


		BoardModel resBoard = ai0.playTurn(0, board.data_);
		
		assertEquals(resBoard.getNumCardsHandCurrentPlayer(), 0); //1 card drawn from Loot Horder attacking and dying, then played the drawn card
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getNumMinions(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(resBoard).getNumMinions(), 0); //1 minion should have been killed
		assertEquals(resBoard.getCurrentPlayer().getMana(), 1); //2 mana used
		assertEquals(resBoard.getWaitingPlayer().getMana(), 3);
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

		board.data_.getCurrentPlayer().setMana((byte)3);
		board.data_.getWaitingPlayer().setMana((byte)3);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)3);
		board.data_.getWaitingPlayer().setMaxMana((byte)3);


		BoardModel resBoard = ai0.playTurn(0, board.data_);
		
		assertEquals(resBoard.getNumCardsHandCurrentPlayer(), 0); //no cards in hand
		assertEquals(resBoard.getNumCardsHandWaitingPlayer(), 1); //drew cards from the loot horder that was killed
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getNumMinions(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(resBoard).getNumMinions(), 0); //1 minion should have been killed
		assertEquals(resBoard.getCurrentPlayer().getMana(), 3); //no mana used
		assertEquals(resBoard.getWaitingPlayer().getMana(), 3);
		assertEquals(resBoard.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(resBoard.getWaitingPlayerHero().getHealth(), 30);
	}
}
