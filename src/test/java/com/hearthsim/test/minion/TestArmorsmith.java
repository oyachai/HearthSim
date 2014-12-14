package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.Armorsmith;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.minion.concrete.StormwindChampion;
import com.hearthsim.card.spellcard.concrete.HolySmite;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestArmorsmith {

	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() throws HSException {
		board = new HearthTreeNode(new BoardModel());

		Minion minion0_0 = new StormwindChampion();
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

		board.data_.getCurrentPlayer().setMana((byte)20);
		board.data_.getWaitingPlayer().setMana((byte)20);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)10);
		board.data_.getWaitingPlayer().setMaxMana((byte)10);
		
		HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
		tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);
		tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);

		board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
		board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayerHero(), board, deck, null);
		board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayerHero(), board, deck, null);

		board.data_.resetMana();
		board.data_.resetMinions();
		
		Minion fb = new Armorsmith();
		board.data_.placeCardHandCurrentPlayer(fb);

	}
	

	@Test
	public void test0() throws HSException {
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, deck, null);
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth(), 5);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth(), 3);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalHealth(), 6);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth(), 2);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 3);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 3);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalAttack(), 7);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 4);
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getAuraAttack(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getAuraAttack(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getAuraAttack(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getAuraAttack(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getAuraAttack(), 1);

		
		
		
		
		board.data_.placeCardHandCurrentPlayer(new HolySmite());
		theCard = board.data_.getCurrentPlayerCardHand(0);
		ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 3, ret, deck, null);
		assertFalse(ret == null);

		assertEquals(ret.data_.getNumCards_hand(), 0);
		assertEquals(ret.data_.getCurrentPlayer().getNumMinions(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getNumMinions(), 2);
		assertEquals(ret.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(ret.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(ret.data_.getCurrentPlayerHero().getArmor(), 1);
		assertEquals(ret.data_.getWaitingPlayerHero().getArmor(), 0);

		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(0).getTotalHealth(), 5);
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(1).getTotalHealth(), 3);
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(2).getTotalHealth(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(0).getTotalHealth(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(1).getTotalHealth(), 2);
		
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 3);
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 3);
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 7);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(1).getTotalAttack(), 4);
		
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(0).getAuraAttack(), 2);
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(1).getAuraAttack(), 1);
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(2).getAuraAttack(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(0).getAuraAttack(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(1).getAuraAttack(), 1);

		
		
		
		board.data_.placeCardHandCurrentPlayer(new HolySmite());
		theCard = board.data_.getCurrentPlayerCardHand(0);
		ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 2, ret, deck, null);
		assertFalse(ret == null);
		assertEquals(ret.data_.getNumCards_hand(), 0);
		assertEquals(ret.data_.getCurrentPlayer().getNumMinions(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getNumMinions(), 1);
		assertEquals(ret.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(ret.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(ret.data_.getCurrentPlayerHero().getArmor(), 1);
		assertEquals(ret.data_.getWaitingPlayerHero().getArmor(), 0);

		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(0).getTotalHealth(), 5);
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(1).getTotalHealth(), 3);
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(2).getTotalHealth(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(0).getTotalHealth(), 2);
		
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 3);
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 3);
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 7);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(0).getTotalAttack(), 2);
		
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(0).getAuraAttack(), 2);
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(1).getAuraAttack(), 1);
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(2).getAuraAttack(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(0).getAuraAttack(), 0);
		
		
		ret = new HearthTreeNode(ret.data_.flipPlayers());
		ret.data_.placeCardHandCurrentPlayer(new HolySmite());
		theCard = ret.data_.getCurrentPlayerCardHand(0);
		ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 2, ret, deck, null);
		assertFalse(ret == null);
		assertEquals(ret.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getNumMinions(), 3);
		assertEquals(ret.data_.getCurrentPlayer().getNumMinions(), 1);
		assertEquals(ret.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(ret.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(ret.data_.getWaitingPlayerHero().getArmor(), 2);
		assertEquals(ret.data_.getCurrentPlayerHero().getArmor(), 0);

		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(0).getTotalHealth(), 5);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(1).getTotalHealth(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(2).getTotalHealth(), 4);
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(0).getTotalHealth(), 2);
		
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(0).getTotalAttack(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(1).getTotalAttack(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(2).getTotalAttack(), 7);
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 2);
		
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(0).getAuraAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(1).getAuraAttack(), 1);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(ret).getMinions().get(2).getAuraAttack(), 1);
		assertEquals(ret.data_.getCurrentPlayer().getMinions().get(0).getAuraAttack(), 0);
	}
	
}
