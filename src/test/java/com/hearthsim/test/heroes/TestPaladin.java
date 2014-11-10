package com.hearthsim.test.heroes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.minion.concrete.SilverHandRecruit;
import com.hearthsim.card.minion.heroes.Paladin;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.card.spellcard.concrete.WildGrowth;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestPaladin {

	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() {
		board = new HearthTreeNode(new BoardModel(new Paladin(), new TestHero()));

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

		Card fb = new WildGrowth();
		board.data_.placeCardHandCurrentPlayer(fb);

		board.data_.getCurrentPlayer().setMana((byte)9);
		board.data_.getWaitingPlayer().setMana((byte)9);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)8);
		board.data_.getWaitingPlayer().setMaxMana((byte)8);
		
		HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
		try {
			tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER,
					tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);
			tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER,
					tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);
		} catch (HSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
		try {
			board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER,
					board.data_.getCurrentPlayerHero(), board, deck, null);
			board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER,
					board.data_.getCurrentPlayerHero(), board, deck, null);
		} catch (HSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board.data_.resetMana();
		board.data_.resetMinions();

	}

	@Test
	public void testHeropower() throws HSException {
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		Hero paladin = board.data_.getCurrentPlayerHero();

		HearthTreeNode ret = paladin.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertEquals(board, ret);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(ret).getNumMinions(), 3);
		assertEquals(ret.data_.getCurrentPlayer().getMana(), 6);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(ret).getMinions().get(0).getHealth(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(ret).getMinions().get(1).getHealth(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(ret).getMinions().get(2).getHealth(), 1);

		// buffed by Raid Leader
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(ret).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(ret).getMinions().get(1).getTotalAttack(), 7);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(ret).getMinions().get(2).getTotalAttack(), 2);
	}

	@Test
	public void testHeropowerWithFullBoard() throws HSException {
		Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		Hero paladin = board.data_.getCurrentPlayerHero();
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
		board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 7);

		HearthTreeNode ret = paladin.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertNull(ret);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 7);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 8);
	}

	@Test
	public void testHeropowerCannotTargetMinion() throws HSException {
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 2);
		Hero paladin = board.data_.getCurrentPlayerHero();

		HearthTreeNode ret = paladin.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertNull(ret);

		assertEquals(board.data_.getCurrentPlayer().getMana(), 8);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
	}

	@Test
	public void testHeropowerCannotTargetOpponent() throws HSException {
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		Hero paladin = board.data_.getCurrentPlayerHero();

		HearthTreeNode ret = paladin.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertNull(ret);

		assertEquals(board.data_.getCurrentPlayer().getMana(), 8);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
	}
}
