package com.hearthsim.test.minion;

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
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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

		board.data_.getCurrentPlayer().setMana((byte)18);
		board.data_.getWaitingPlayer().setMana((byte)18);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)8);
		board.data_.getWaitingPlayer().setMaxMana((byte)8);
		
		minion0_0.summonMinion(PlayerSide.CURRENT_PLAYER, board.data_.getHero(PlayerSide.CURRENT_PLAYER), board, null, null, false, true);
		minion0_1.summonMinion(PlayerSide.CURRENT_PLAYER, board.data_.getHero(PlayerSide.CURRENT_PLAYER), board, null, null, false, true);
		minion0_2.summonMinion(PlayerSide.CURRENT_PLAYER, board.data_.getHero(PlayerSide.CURRENT_PLAYER), board, null, null, false, true);
		minion1_0.summonMinion(PlayerSide.WAITING_PLAYER, board.data_.getHero(PlayerSide.WAITING_PLAYER), board, null, null, false, true);
		minion1_1.summonMinion(PlayerSide.WAITING_PLAYER, board.data_.getHero(PlayerSide.WAITING_PLAYER), board, null, null, false, true);

		minion0_0.setHealth((byte)1);
		minion0_1.setHealth((byte)1);

		minion1_0.setHealth((byte)1);

		board.data_.getHero(PlayerSide.CURRENT_PLAYER).setHealth((byte)20);
		board.data_.getHero(PlayerSide.WAITING_PLAYER).setHealth((byte)20);
		
		board.data_.resetMana();

	}
	
	@Test
	public void test0() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 0, board, deck, null);
		
		assertNull(ret);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 8);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 8);
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
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 3, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 5);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 8);
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
		assertEquals(c0.data_.getCurrentPlayer().getMana(), 5);
		assertEquals(c0.data_.getWaitingPlayer().getMana(), 8);
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
		assertEquals(c1.data_.getCurrentPlayer().getMana(), 5);
		assertEquals(c1.data_.getWaitingPlayer().getMana(), 8);
		assertEquals(c1.data_.getCurrentPlayerHero().getHealth(), 20);
		assertEquals(c1.data_.getWaitingPlayerHero().getHealth(), 23);
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
		HearthTreeNode c3 = ret.getChildren().get(3);
		assertEquals(c3.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c3).getNumMinions(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c3).getNumMinions(), 2);
		assertEquals(c3.data_.getCurrentPlayer().getMana(), 5);
		assertEquals(c3.data_.getWaitingPlayer().getMana(), 8);
		assertEquals(c3.data_.getCurrentPlayerHero().getHealth(), 20);
		assertEquals(c3.data_.getWaitingPlayerHero().getHealth(), 20);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c3).getMinions().get(0).getTotalHealth(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c3).getMinions().get(1).getTotalHealth(), 4);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c3).getMinions().get(2).getTotalHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c3).getMinions().get(3).getTotalHealth(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c3).getMinions().get(0).getTotalHealth(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c3).getMinions().get(1).getTotalHealth(), 1);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c3).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c3).getMinions().get(1).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c3).getMinions().get(2).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c3).getMinions().get(3).getTotalAttack(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c3).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c3).getMinions().get(1).getTotalAttack(), 7);

		//------------------------------------------------------------------------------------------------

		HearthTreeNode c5 = ret.getChildren().get(5);
		assertEquals(c5.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c5).getNumMinions(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c5).getNumMinions(), 2);
		assertEquals(c5.data_.getCurrentPlayer().getMana(), 5);
		assertEquals(c5.data_.getWaitingPlayer().getMana(), 8);
		assertEquals(c5.data_.getCurrentPlayerHero().getHealth(), 20);
		assertEquals(c5.data_.getWaitingPlayerHero().getHealth(), 20);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c5).getMinions().get(0).getTotalHealth(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c5).getMinions().get(1).getTotalHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c5).getMinions().get(2).getTotalHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c5).getMinions().get(3).getTotalHealth(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c5).getMinions().get(0).getTotalHealth(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c5).getMinions().get(1).getTotalHealth(), 1);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c5).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c5).getMinions().get(1).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c5).getMinions().get(2).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c5).getMinions().get(3).getTotalAttack(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c5).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c5).getMinions().get(1).getTotalAttack(), 7);

		//------------------------------------------------------------------------------------------------
		HearthTreeNode c6 = ret.getChildren().get(6);
		assertEquals(c6.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(c6).getNumMinions(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(c6).getNumMinions(), 2);
		assertEquals(c6.data_.getCurrentPlayer().getMana(), 5);
		assertEquals(c6.data_.getWaitingPlayer().getMana(), 8);
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
