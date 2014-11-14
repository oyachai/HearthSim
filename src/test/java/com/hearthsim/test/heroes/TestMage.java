package com.hearthsim.test.heroes;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.ChillwindYeti;
import com.hearthsim.card.minion.heroes.Mage;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestMage {


	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() {
		board = new HearthTreeNode(new BoardModel(new Mage(), new TestHero()));

		Minion minion0_0 = new ChillwindYeti();
		Minion minion0_1 = new BoulderfistOgre();
		Minion minion1_0 = new ChillwindYeti();
		Minion minion1_1 = new BoulderfistOgre();
		
		board.data_.placeCardHandCurrentPlayer(minion0_1);
		board.data_.placeCardHandCurrentPlayer(minion0_0);
				
		board.data_.placeCardHandWaitingPlayer(minion1_1);
		board.data_.placeCardHandWaitingPlayer(minion1_0);

		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}
	
		deck = new Deck(cards);

		board.data_.getCurrentPlayer().setMana((byte)11);
		board.data_.getWaitingPlayer().setMana((byte)11);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)8);
		board.data_.getWaitingPlayer().setMaxMana((byte)8);
		
		HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
		try {
			tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);
			tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);
		} catch (HSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
		try {
			board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayerHero(), board, deck, null);
			board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayerHero(), board, deck, null);
		} catch (HSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board.data_.resetMana();
		board.data_.resetMinions();
		
	}

	@Test
	public void testHeropowerAgainstOpponent() throws HSException {
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0); // Opponent hero
		Hero mage = board.data_.getCurrentPlayerHero();

		HearthTreeNode ret = mage.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertEquals(board, ret);

		assertEquals(board.data_.getCurrentPlayer().getMana(), 6);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 29);
	}

	@Test
	public void testHeropowerAgainstMinion() throws HSException {
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 1); // Yeti
		Hero mage = board.data_.getCurrentPlayerHero();

		HearthTreeNode ret = mage.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertEquals(board, ret);

		assertEquals(board.data_.getCurrentPlayer().getMana(), 6);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 5);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 7);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 7);
	}

	@Test
	public void testHeropowerAgainstSelf() throws HSException {
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0); // Self
		Hero mage = board.data_.getCurrentPlayerHero();

		HearthTreeNode ret = mage.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertEquals(board, ret);

		assertEquals(board.data_.getCurrentPlayer().getMana(), 6);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 29);
	}

	@Test
	public void testHeropowerAgainstOwnMinion() throws HSException {
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 1); // Yeti
		Hero mage = board.data_.getCurrentPlayerHero();

		HearthTreeNode ret = mage.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertEquals(board, ret);

		assertEquals(board.data_.getCurrentPlayer().getMana(), 6);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 4);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 7);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 5);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 7);
	}

	@Test
	public void testHeropowerKillsMinion() throws HSException {
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 1); // Yeti
		target.setHealth((byte)1);

		Hero mage = board.data_.getCurrentPlayerHero();

		HearthTreeNode ret = mage.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertEquals(board, ret);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 1);

		assertEquals(board.data_.getCurrentPlayer().getMana(), 6);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 5);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 7);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 7);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 4);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 6);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 6);
	}
}
