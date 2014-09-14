package com.hearthsim.test.heroes;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.minion.heroes.Paladin;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.card.spellcard.concrete.WildGrowth;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestPaladin {


	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() {
		board = new HearthTreeNode(new BoardModel(new Paladin(), new Hero()));

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

		board.data_.setMana_p0((byte)8);
		board.data_.setMana_p1((byte)8);
		
		board.data_.setMaxMana_p0((byte)8);
		board.data_.setMaxMana_p1((byte)8);
		
		HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
		try {
			tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(tmpBoard.data_.getCurrentPlayer(), tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);
			tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(tmpBoard.data_.getCurrentPlayer(), tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);
		} catch (HSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
		try {
			board.data_.getCurrentPlayerCardHand(0).useOn(board.data_.getCurrentPlayer(), board.data_.getCurrentPlayerHero(), board, deck, null);
			board.data_.getCurrentPlayerCardHand(0).useOn(board.data_.getCurrentPlayer(), board.data_.getCurrentPlayerHero(), board, deck, null);
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
		Minion target = board.data_.getCharacter(board.data_.getWaitingPlayer(), 0);
		Minion minion = board.data_.getCurrentPlayer().getMinions().get(0);
		HearthTreeNode ret = minion.attack(board.data_.getWaitingPlayer(), target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getCurrentPlayer().getNumMinions(), 2);
		assertEquals(board.data_.getWaitingPlayer().getNumMinions(), 2);
		assertEquals(board.data_.getMana_p0(), 8);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 28);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getHealth(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getHealth(), 7);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getHealth(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getHealth(), 7);

		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 7);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 7);
		
		
		Hero hero = board.data_.getCurrentPlayerHero();
		target = board.data_.getCharacter(board.data_.getWaitingPlayer(), 0);
		ret = hero.useHeroAbility(board.data_.getWaitingPlayer(), target, board, deck, null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getCurrentPlayer().getNumMinions(), 2);
		assertEquals(board.data_.getWaitingPlayer().getNumMinions(), 2);
		assertEquals(board.data_.getMana_p0(), 8);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 28);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getHealth(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getHealth(), 7);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getHealth(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getHealth(), 7);

		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 7);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 7);
		
		minion.hasAttacked(false);
		target = board.data_.getCharacter(board.data_.getWaitingPlayer(), 2);
		ret = minion.attack(board.data_.getWaitingPlayer(), target, board, deck, null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getCurrentPlayer().getNumMinions(), 1);
		assertEquals(board.data_.getWaitingPlayer().getNumMinions(), 2);
		assertEquals(board.data_.getMana_p0(), 8);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 28);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getHealth(), 7);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getHealth(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getHealth(), 5);

		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 6);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 7);
		
		minion.hasAttacked(false);
		target = board.data_.getCharacter(board.data_.getCurrentPlayer(), 0);
		ret = hero.useHeroAbility(board.data_.getCurrentPlayer(), target, board, deck, null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getCurrentPlayer().getNumMinions(), 2);
		assertEquals(board.data_.getWaitingPlayer().getNumMinions(), 2);
		assertEquals(board.data_.getMana_p0(), 6);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 28);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getHealth(), 7);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getHealth(), 1);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getHealth(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getHealth(), 5);

		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 6);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 1);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 7);
		
	}
}
