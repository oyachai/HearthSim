package com.hearthsim.test.heroes;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.ChillwindYeti;
import com.hearthsim.card.minion.heroes.Priest;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestPriest {


	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() {
		board = new HearthTreeNode(new BoardModel(new Priest(), new Hero()));

		Minion minion0_0 = new ChillwindYeti();
		Minion minion0_1 = new ChillwindYeti();
		Minion minion1_0 = new ChillwindYeti();
		Minion minion1_1 = new ChillwindYeti();
		
		board.data_.placeCardHandCurrentPlayer(minion0_0);
		board.data_.placeCardHandCurrentPlayer(minion0_1);
				
		board.data_.placeCardHandWaitingPlayer(minion1_0);
		board.data_.placeCardHandWaitingPlayer(minion1_1);

		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}
	
		deck = new Deck(cards);

		board.data_.getCurrentPlayer().setMana((byte)8);
		board.data_.getWaitingPlayer().setMana((byte)8);
		
		board.data_.getCurrentPlayer().setMaxMana((byte)8);
		board.data_.getWaitingPlayer().setMaxMana((byte)8);
		
		HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
		try {
			tmpBoard.data_.getCurrentPlayerCardHand(0).getCardAction().useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);
			tmpBoard.data_.getCurrentPlayerCardHand(0).getCardAction().useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);
		} catch (HSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
		try {
			board.data_.getCurrentPlayerCardHand(0).getCardAction().useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayerHero(), board, deck, null);
			board.data_.getCurrentPlayerCardHand(0).getCardAction().useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayerHero(), board, deck, null);
		} catch (HSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board.data_.resetMana();
		board.data_.resetMinions();
		
	}
	
	@Test
	public void test0() throws HSException {
		
		//null case
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		Minion minion = PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0);
		HearthTreeNode ret = minion.attack(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 8);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 8);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 26);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 5);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 5);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 5);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 5);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 4);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 4);
		
		
		target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		Hero hero = board.data_.getCurrentPlayerHero();
		ret = hero.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 6);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 8);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 28);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 5);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 5);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 5);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 5);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 4);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 4);
		
		minion.hasAttacked(false);
		target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 2);
		ret = minion.attack(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 6);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 8);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 28);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 5);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 5);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 1);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 4);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 4);
		
		minion.hasAttacked(false);
		target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 2);
		ret = hero.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 4);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 8);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 28);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 5);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 5);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 3);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 4);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 4);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 4);
		
	}
	
	@Test
	public void test1() throws HSException {
		

		
		BoardStateFactoryBase factory = new BoardStateFactoryBase(null, null, 2000000000);
		HearthTreeNode tree = new HearthTreeNode(board.data_);
        ArtificialPlayer ai0 = ArtificialPlayer.buildStandardAI1();
		try {
			tree = factory.doMoves(tree, ai0);
		} catch (HSException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		
		//best move is to kill one enemy yeti with 2 of your own yeti and heal one of your own yeti
		HearthTreeNode bestPlay = tree.findMaxOfFunc(ai0);

		assertFalse(tree == null);
		assertEquals(bestPlay.data_.getNumCards_hand(), 0);
		assertEquals(bestPlay.data_.getCurrentPlayer().getNumMinions(), 2);
		assertEquals(bestPlay.data_.getWaitingPlayer().getNumMinions(), 1);
		assertEquals(bestPlay.data_.getCurrentPlayer().getMana(), 6);
		assertEquals(bestPlay.data_.getWaitingPlayer().getMana(), 8);
		assertEquals(bestPlay.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(bestPlay.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(bestPlay.data_.getCurrentPlayer().getMinions().get(0).getHealth(), 3);
		assertEquals(bestPlay.data_.getCurrentPlayer().getMinions().get(1).getHealth(), 1);
		assertEquals(bestPlay.data_.getWaitingPlayer().getMinions().get(0).getHealth(), 5);

		assertEquals(bestPlay.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 4);
		assertEquals(bestPlay.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 4);
		assertEquals(bestPlay.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 4);
		
	}
	
	@Test
	public void test2() throws HSException {
		
		
		//one of your yeti is damaged
		PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).setHealth((byte)3);
		

		
		BoardStateFactoryBase factory = new BoardStateFactoryBase(null, null, 2000000000);
		HearthTreeNode tree = new HearthTreeNode(board.data_);
        ArtificialPlayer ai0 = ArtificialPlayer.buildStandardAI1();
		try {
			tree = factory.doMoves(tree, ai0);
		} catch (HSException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		
		//best move is to heal the damaged yeti and attack with both to kill one of the enemy's yeti
		HearthTreeNode bestPlay = tree.findMaxOfFunc(ai0);

		assertFalse(tree == null);
		assertEquals(bestPlay.data_.getNumCards_hand(), 0);
		assertEquals(bestPlay.data_.getCurrentPlayer().getNumMinions(), 2);
		assertEquals(bestPlay.data_.getWaitingPlayer().getNumMinions(), 1);
		assertEquals(bestPlay.data_.getCurrentPlayer().getMana(), 6);
		assertEquals(bestPlay.data_.getWaitingPlayer().getMana(), 8);
		assertEquals(bestPlay.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(bestPlay.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(bestPlay.data_.getCurrentPlayer().getMinions().get(0).getHealth(), 1);
		assertEquals(bestPlay.data_.getCurrentPlayer().getMinions().get(1).getHealth(), 1);
		assertEquals(bestPlay.data_.getWaitingPlayer().getMinions().get(0).getHealth(), 5);

		assertEquals(bestPlay.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 4);
		assertEquals(bestPlay.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 4);
		assertEquals(bestPlay.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 4);
		
	}
}
