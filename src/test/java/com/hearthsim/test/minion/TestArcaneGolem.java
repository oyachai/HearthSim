package com.hearthsim.test.minion;

import java.util.List;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.ArcaneGolem;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.util.HearthActionBoardPair;
import com.hearthsim.util.tree.HearthTreeNode;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestArcaneGolem {

	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel());

		Minion minion0_0 = new BoulderfistOgre();
		Minion minion0_1 = new RaidLeader();
		Minion minion1_0 = new BoulderfistOgre();
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

		Card fb = new ArcaneGolem();
		board.data_.placeCardHandCurrentPlayer(fb);

		board.data_.getCurrentPlayer().setMana((byte)18);
		board.data_.getWaitingPlayer().setMana((byte)18);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)8);
		board.data_.getWaitingPlayer().setMaxMana((byte)8);
		
		HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
		tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);
		tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);

		board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
		board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayerHero(), board, deck, null);
		board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayerHero(), board, deck, null);

		board.data_.resetMana();
		board.data_.resetMinions();
		
	}
	
	@Test
	public void testIncreasesEnemyMana() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, deck, null);
		assertEquals(board, ret);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 5);
		assertEquals(board.data_.getCurrentPlayer().getMaxMana(), 8);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 9);
		assertEquals(board.data_.getWaitingPlayer().getMaxMana(), 9);
	}

	@Test
	@Ignore("Existing bug")
	public void testDoesNotIncreaseOverTen() throws HSException {
		board.data_.getWaitingPlayer().setMaxMana((byte)10);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, deck, null);
		assertEquals(board, ret);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 9);
		assertEquals(board.data_.getWaitingPlayer().getMaxMana(), 10);
	}

	@Test
	public void testAiAttacksAfterPlaying() throws HSException {
		board.data_.getCurrentPlayer().setMana((byte)3);
		board.data_.getWaitingPlayer().setMana((byte)3);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)3);
		board.data_.getWaitingPlayer().setMaxMana((byte)3);

		board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 1).hasAttacked(true);
		board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 2).hasAttacked(true);

        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
        List<HearthActionBoardPair> ab = ai0.playTurn(0, board.data_);
		BoardModel resBoard = ab.get(ab.size() - 1).board;
		
		assertEquals(resBoard.getNumCardsHandCurrentPlayer(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getNumMinions(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(resBoard).getNumMinions(), 2); //1 minion should have been killed
		assertEquals(resBoard.getCurrentPlayer().getMana(), 0); //3 mana used
		assertEquals(resBoard.getWaitingPlayer().getMana(), 4); //1 mana given by the Arcane Golem
		assertEquals(resBoard.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(resBoard.getWaitingPlayerHero().getHealth(), 25); //5 damage to the face... 4 from Arcane Golem (he has charge!), 1 more from Raid Leader's buff
	}
}
