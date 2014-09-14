package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.ArcaneGolem;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestArcaneGolem {

	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() {
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
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(board.data_.getWaitingPlayer(), target, board, deck, null);
		
		assertTrue(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getCurrentPlayer().getNumMinions(), 2);
		assertEquals(board.data_.getWaitingPlayer().getNumMinions(), 2);
		assertEquals(board.data_.getMana_p0(), 8);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getHealth(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getHealth(), 7);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getHealth(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getHealth(), 7);

		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 7);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 7);
	}
	
	@Test
	public void test1() throws HSException {
		
		//null case
		Minion target = board.data_.getCharacter(board.data_.getCurrentPlayer(), 2);
		Card theCard = board.data_.getCurrentPlayerCardHand(0);
		HearthTreeNode ret = theCard.useOn(board.data_.getCurrentPlayer(), target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getCurrentPlayer().getNumMinions(), 3);
		assertEquals(board.data_.getWaitingPlayer().getNumMinions(), 2);
		assertEquals(board.data_.getMana_p0(), 5);
		assertEquals(board.data_.getMana_p1(), 9);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getHealth(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getHealth(), 7);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(2).getHealth(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getHealth(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getHealth(), 7);

		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 7);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 5);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 7);
		
		
		
		//----------------------------------------------------------
		
		Minion minion = board.data_.getCurrentPlayer().getMinions().get(2);
		
		minion.hasAttacked(false);
		target = board.data_.getCharacter(board.data_.getWaitingPlayer(), 2);
		minion.attack(board.data_.getWaitingPlayer(), target, board, deck, null);

		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getCurrentPlayer().getNumMinions(), 2);
		assertEquals(board.data_.getWaitingPlayer().getNumMinions(), 2);
		assertEquals(board.data_.getMana_p0(), 5);
		assertEquals(board.data_.getMana_p1(), 9);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getHealth(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getHealth(), 7);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getHealth(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getHealth(), 7 - 5);

		assertEquals(board.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 7);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(board.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 7);
		
		
	}


	@Test
	public void test2() throws HSException {
		
		Hero hero = new Hero();
		PlayerModel playerModel0 = new PlayerModel("player0", hero, deck);
		PlayerModel playerModel1 = new PlayerModel("player0", hero, deck);
		
		board.data_.setMana_p0((byte)3);
		board.data_.setMana_p1((byte)3);
		
		board.data_.setMaxMana_p0((byte)3);
		board.data_.setMaxMana_p1((byte)3);

		board.data_.getCharacter(board.data_.getCurrentPlayer(), 1).hasAttacked(true);
		board.data_.getCharacter(board.data_.getCurrentPlayer(), 2).hasAttacked(true);

        ArtificialPlayer ai0 = ArtificialPlayer.buildStandardAI1();
		BoardModel resBoard = ai0.playTurn(0, board.data_, playerModel0, playerModel1);
		
		assertEquals(resBoard.getNumCardsHandCurrentPlayer(), 0);
		assertEquals(resBoard.getCurrentPlayer().getNumMinions(), 3);
		assertEquals(resBoard.getWaitingPlayer().getNumMinions(), 2); //1 minion should have been killed
		assertEquals(resBoard.getMana_p0(), 0); //3 mana used
		assertEquals(resBoard.getMana_p1(), 4); //1 mana given by the Arcane Golem
		assertEquals(resBoard.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(resBoard.getWaitingPlayerHero().getHealth(), 25); //5 damage to the face... 4 from Arcane Golem (he has charge!), 1 more from Raid Leader's buff
	}
}
