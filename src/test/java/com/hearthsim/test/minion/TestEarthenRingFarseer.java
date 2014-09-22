package com.hearthsim.test.minion;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.DreadInfernal;
import com.hearthsim.card.minion.concrete.EarthenRingFarseer;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestEarthenRingFarseer {

	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() throws HSInvalidPlayerIndexException, HSException {
		board = new HearthTreeNode(new BoardModel());

		Minion minion0_0 = new DreadInfernal();
		Minion minion0_1 = new BoulderfistOgre();
		Minion minion0_2 = new RaidLeader();
		Minion minion1_0 = new BoulderfistOgre();
		Minion minion1_1 = new RaidLeader();
		
		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}
	
		deck = new Deck(cards);

		Card fb = new EarthenRingFarseer();
		board.data_.placeCardHandCurrentPlayer(fb);

		board.data_.setMana_p0((byte)8);
		board.data_.setMana_p1((byte)8);
		
		board.data_.setMaxMana_p0((byte)8);
		board.data_.setMaxMana_p1((byte)8);
		
		minion0_0.summonMinion(PlayerSide.CURRENT_PLAYER, board.data_.getHero(PlayerSide.CURRENT_PLAYER), board, null, null, false);
		minion0_1.summonMinion(PlayerSide.CURRENT_PLAYER, board.data_.getHero(PlayerSide.CURRENT_PLAYER), board, null, null, false);
		minion0_2.summonMinion(PlayerSide.CURRENT_PLAYER, board.data_.getHero(PlayerSide.CURRENT_PLAYER), board, null, null, false);
		minion1_0.summonMinion(PlayerSide.WAITING_PLAYER, board.data_.getHero(PlayerSide.WAITING_PLAYER), board, null, null, false);
		minion1_1.summonMinion(PlayerSide.WAITING_PLAYER, board.data_.getHero(PlayerSide.WAITING_PLAYER), board, null, null, false);

		minion0_0.setHealth((byte)1);
		minion0_1.setHealth((byte)1);

		minion1_0.setHealth((byte)1);

		board.data_.getHero(PlayerSide.CURRENT_PLAYER).setHealth((byte)20);
		board.data_.getHero(PlayerSide.WAITING_PLAYER).setHealth((byte)20);
	}
	
	@Test
	public void test0() throws HSException {
		
		//null case
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		
		assertTrue(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(board.data_.getMana_p0(), 8);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 20);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 20);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalHealth(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth(), 1);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalAttack(), 7);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 7);
	}

	@Test
	public void test1() throws HSException {
		
		//null case
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 3);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(board.data_.getMana_p0(), 5);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 20);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 20);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(3).getTotalHealth(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth(), 1);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(3).getTotalAttack(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 7);

		
		//------------------------------------------------------------------------------------------------
		HearthTreeNode c0 = ret.getChildren().get(0);
		assertEquals(c0.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c0).getNumMinions(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c0).getNumMinions(), 2);
		assertEquals(c0.data_.getMana_p0(), 5);
		assertEquals(c0.data_.getMana_p1(), 8);
		assertEquals(c0.data_.getCurrentPlayerHero().getHealth(), 23);
		assertEquals(c0.data_.getWaitingPlayerHero().getHealth(), 20);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c0).getMinions().get(0).getTotalHealth(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c0).getMinions().get(1).getTotalHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c0).getMinions().get(2).getTotalHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c0).getMinions().get(3).getTotalHealth(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c0).getMinions().get(0).getTotalHealth(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c0).getMinions().get(1).getTotalHealth(), 1);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c0).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c0).getMinions().get(1).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c0).getMinions().get(2).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c0).getMinions().get(3).getTotalAttack(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c0).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c0).getMinions().get(1).getTotalAttack(), 7);

		//------------------------------------------------------------------------------------------------
		HearthTreeNode c1 = ret.getChildren().get(1);
		assertEquals(c1.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c1).getNumMinions(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c1).getNumMinions(), 2);
		assertEquals(c1.data_.getMana_p0(), 5);
		assertEquals(c1.data_.getMana_p1(), 8);
		assertEquals(c1.data_.getCurrentPlayerHero().getHealth(), 20);
		assertEquals(c1.data_.getWaitingPlayerHero().getHealth(), 20);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c1).getMinions().get(0).getTotalHealth(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c1).getMinions().get(1).getTotalHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c1).getMinions().get(2).getTotalHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c1).getMinions().get(3).getTotalHealth(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c1).getMinions().get(0).getTotalHealth(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c1).getMinions().get(1).getTotalHealth(), 1);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c1).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c1).getMinions().get(1).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c1).getMinions().get(2).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c1).getMinions().get(3).getTotalAttack(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c1).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c1).getMinions().get(1).getTotalAttack(), 7);

		//------------------------------------------------------------------------------------------------
		HearthTreeNode c2 = ret.getChildren().get(2);
		assertEquals(c2.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c2).getNumMinions(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c2).getNumMinions(), 2);
		assertEquals(c2.data_.getMana_p0(), 5);
		assertEquals(c2.data_.getMana_p1(), 8);
		assertEquals(c2.data_.getCurrentPlayerHero().getHealth(), 20);
		assertEquals(c2.data_.getWaitingPlayerHero().getHealth(), 20);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c2).getMinions().get(0).getTotalHealth(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c2).getMinions().get(1).getTotalHealth(), 4);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c2).getMinions().get(2).getTotalHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c2).getMinions().get(3).getTotalHealth(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c2).getMinions().get(0).getTotalHealth(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c2).getMinions().get(1).getTotalHealth(), 1);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c2).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c2).getMinions().get(1).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c2).getMinions().get(2).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c2).getMinions().get(3).getTotalAttack(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c2).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c2).getMinions().get(1).getTotalAttack(), 7);

		//------------------------------------------------------------------------------------------------
		HearthTreeNode c4 = ret.getChildren().get(4);
		assertEquals(c4.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c4).getNumMinions(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c4).getNumMinions(), 2);
		assertEquals(c4.data_.getMana_p0(), 5);
		assertEquals(c4.data_.getMana_p1(), 8);
		assertEquals(c4.data_.getCurrentPlayerHero().getHealth(), 20);
		assertEquals(c4.data_.getWaitingPlayerHero().getHealth(), 23);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c4).getMinions().get(0).getTotalHealth(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c4).getMinions().get(1).getTotalHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c4).getMinions().get(2).getTotalHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c4).getMinions().get(3).getTotalHealth(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c4).getMinions().get(0).getTotalHealth(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c4).getMinions().get(1).getTotalHealth(), 1);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c4).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c4).getMinions().get(1).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c4).getMinions().get(2).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c4).getMinions().get(3).getTotalAttack(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c4).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c4).getMinions().get(1).getTotalAttack(), 7);

		//------------------------------------------------------------------------------------------------
		HearthTreeNode c6 = ret.getChildren().get(6);
		assertEquals(c6.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c6).getNumMinions(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c6).getNumMinions(), 2);
		assertEquals(c6.data_.getMana_p0(), 5);
		assertEquals(c6.data_.getMana_p1(), 8);
		assertEquals(c6.data_.getCurrentPlayerHero().getHealth(), 20);
		assertEquals(c6.data_.getWaitingPlayerHero().getHealth(), 20);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c6).getMinions().get(0).getTotalHealth(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c6).getMinions().get(1).getTotalHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c6).getMinions().get(2).getTotalHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c6).getMinions().get(3).getTotalHealth(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c6).getMinions().get(0).getTotalHealth(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c6).getMinions().get(1).getTotalHealth(), 4);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c6).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c6).getMinions().get(1).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c6).getMinions().get(2).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c6).getMinions().get(3).getTotalAttack(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c6).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c6).getMinions().get(1).getTotalAttack(), 7);
	}

}
