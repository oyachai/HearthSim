package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.*;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestCruelTaskmaster {
	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() throws HSException {
		Card cards[] = new Card[10];
		for(int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}

		deck = new Deck(cards);

		board = new HearthTreeNode(new BoardModel(deck, deck));

		Minion minion0_0 = new StormwindChampion();
		Minion minion0_1 = new RaidLeader();
		Minion minion0_2 = new HarvestGolem();
	
		Minion minion1_0 = new BoulderfistOgre();
		Minion minion1_1 = new RaidLeader();
		Minion minion1_2 = new Abomination();
		Minion minion1_3 = new LootHoarder();
		
		board.data_.placeCardHandCurrentPlayer(minion0_0);
		board.data_.placeCardHandCurrentPlayer(minion0_1);
		board.data_.placeCardHandCurrentPlayer(minion0_2);
				
		board.data_.placeCardHandWaitingPlayer(minion1_0);
		board.data_.placeCardHandWaitingPlayer(minion1_1);
		board.data_.placeCardHandWaitingPlayer(minion1_2);
		board.data_.placeCardHandWaitingPlayer(minion1_3);


		board.data_.getCurrentPlayer().setMana((byte)20);
		board.data_.getWaitingPlayer().setMana((byte)20);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)10);
		board.data_.getWaitingPlayer().setMaxMana((byte)10);
		
		HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
		tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);
		tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);
		tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);
		tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);

		board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
		board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayerHero(), board, deck, null);
		board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayerHero(), board, deck, null);
		board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayerHero(), board, deck, null);

		board.data_.resetMana();
		board.data_.resetMinions();
		
		Minion fb = new CruelTaskmaster();
		board.data_.placeCardHandCurrentPlayer(fb);

	}
	
	
	
	@Test
	public void test0() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 0, board, deck, deck);
		
		assertNull(ret);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 4);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 10);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 10);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth(), 4);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth(), 3);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalHealth(), 6);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(2).getTotalHealth(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(3).getTotalHealth(), 7);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 4);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 3);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalAttack(), 7);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 5);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(2).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(3).getTotalAttack(), 7);
	}
	
	@Test
	public void test1() throws HSException {	
		
		//set the remaining total health of Abomination to 1
		PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).setHealth((byte)1);
		
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, deck, deck);
		
		assertFalse(ret == null);
		assertEquals(ret.data_.getNumCards_hand(), 0);
		assertEquals(ret.data_.getCurrentPlayer().getNumMinions(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getNumMinions(), 4);
		assertEquals(ret.data_.getCurrentPlayer().getMana(), 8);
		assertEquals(ret.data_.getWaitingPlayer().getMana(), 10);
		assertEquals(ret.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(ret.data_.getWaitingPlayerHero().getHealth(), 30);
		
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(0).getTotalHealth(), 3);
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(1).getTotalHealth(), 4);
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(2).getTotalHealth(), 3);
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(3).getTotalHealth(), 6);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(0).getTotalHealth(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(1).getTotalHealth(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(2).getTotalHealth(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(3).getTotalHealth(), 7);

		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 4);
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 4);
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 3);
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(3).getTotalAttack(), 7);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(0).getTotalAttack(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(1).getTotalAttack(), 5);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(2).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(3).getTotalAttack(), 7);
		
		assertEquals(ret.numChildren(), 7);
		
		//Forth child node is the one that kills the Loot Hoarder
		HearthTreeNode cn3 = ret.getChildren().get(3);
		assertEquals(cn3.data_.getNumCardsHandCurrentPlayer(), 0);
		assertEquals(cn3.data_.getNumCardsHandWaitingPlayer(), 1);
		assertEquals(cn3.data_.getCurrentPlayer().getNumMinions(), 4);
		assertEquals(cn3.data_.getWaitingPlayer().getNumMinions(), 3);
		assertEquals(cn3.data_.getCurrentPlayer().getMana(), 8);
		assertEquals(cn3.data_.getWaitingPlayer().getMana(), 10);
		assertEquals(cn3.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(cn3.data_.getWaitingPlayerHero().getHealth(), 30);
		
		assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(0).getTotalHealth(), 3);
		assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(1).getTotalHealth(), 4);
		assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(2).getTotalHealth(), 3);
		assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(3).getTotalHealth(), 6);
//		assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(0).getTotalHealth(), 1);
		assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(0).getTotalHealth(), 1);
		assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(1).getTotalHealth(), 2);
		assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(2).getTotalHealth(), 7);

		assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 4);
		assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 4);
		assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 3);
		assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(3).getTotalAttack(), 7);
//		assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 3);
		assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 5);
		assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 2);
		assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(2).getTotalAttack(), 7);
	
		//Fifth child node is the one that kills the Abomination
		HearthTreeNode cn4 = ret.getChildren().get(4);
		assertEquals(cn4.data_.getNumCardsHandCurrentPlayer(), 0);
		assertEquals(cn4.data_.getNumCardsHandWaitingPlayer(), 1);
		assertEquals(cn4.data_.getCurrentPlayer().getNumMinions(), 4);
		assertEquals(cn4.data_.getWaitingPlayer().getNumMinions(), 1);
		assertEquals(cn4.data_.getCurrentPlayer().getMana(), 8);
		assertEquals(cn4.data_.getWaitingPlayer().getMana(), 10);
		assertEquals(cn4.data_.getCurrentPlayerHero().getHealth(), 28);
		assertEquals(cn4.data_.getWaitingPlayerHero().getHealth(), 28);
		
		assertEquals(cn4.data_.getCurrentPlayer().getMinions().get(0).getTotalHealth(), 1);
		assertEquals(cn4.data_.getCurrentPlayer().getMinions().get(1).getTotalHealth(), 2);
		assertEquals(cn4.data_.getCurrentPlayer().getMinions().get(2).getTotalHealth(), 1);
		assertEquals(cn4.data_.getCurrentPlayer().getMinions().get(3).getTotalHealth(), 4);
		assertEquals(cn4.data_.getWaitingPlayer().getMinions().get(0).getTotalHealth(), 5);

		assertEquals(cn4.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 4);
		assertEquals(cn4.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 4);
		assertEquals(cn4.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 3);
		assertEquals(cn4.data_.getCurrentPlayer().getMinions().get(3).getTotalAttack(), 7);
		assertEquals(cn4.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 6);
	}
}
