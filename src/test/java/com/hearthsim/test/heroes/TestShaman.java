package com.hearthsim.test.heroes;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.*;
import com.hearthsim.card.minion.heroes.Shaman;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.card.spellcard.concrete.WildGrowth;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestShaman {

    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
	
	private HearthTreeNode board;
	private Deck deck;


	@Before
	public void setup() {
		board = new HearthTreeNode(new BoardModel(new Shaman(), new Hero()));

		Minion minion0_0 = new BoulderfistOgre();
		Minion minion0_1 = new RaidLeader();
		Minion minion1_0 = new BoulderfistOgre();
		Minion minion1_1 = new RaidLeader();
		
		board.data_.placeCardHandCurrentPlayer(minion0_0);
		board.data_.placeCardHandCurrentPlayer(minion0_1);
				
		board.data_.placeCardHandWaitingPlayer(minion1_0);
		board.data_.placeCardHandWaitingPlayer(minion1_1);

		Card cards[] = new Card[30];
		for (int index = 0; index < 30; ++index) {
			cards[index] = new TheCoin();
		}
	
		deck = new Deck(cards);

		Card fb = new WildGrowth();
		board.data_.placeCardHandCurrentPlayer(fb);

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
		
		Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
		Minion minion = PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0);
		HearthTreeNode ret = minion.attack(PlayerSide.WAITING_PLAYER, target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 8);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 8);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 28);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 7);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 7);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 7);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 7);
		
		
		target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
		Hero hero = board.data_.getCurrentPlayerHero();
		ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		
		assertTrue(ret instanceof RandomEffectNode);
		assertEquals(ret.getChildren().size(), 4);
		
		
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
		assertEquals(board.data_.getCurrentPlayer().getMana(), 8);
		assertEquals(board.data_.getWaitingPlayer().getMana(), 8);
		assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 28);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 7);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), 7);

		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 7);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 7);

		HearthTreeNode cn0 = ret.getChildren().get(0);
		assertEquals(cn0.data_.getCurrentPlayer().getNumMinions(), 3);
		assertEquals(cn0.data_.getWaitingPlayer().getNumMinions(), 2);
		assertEquals(cn0.data_.getCurrentPlayer().getMana(), 6);
		assertEquals(cn0.data_.getWaitingPlayer().getMana(), 8);
		assertEquals(cn0.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(cn0.data_.getWaitingPlayerHero().getHealth(), 28);
		
		assertTrue(cn0.data_.getCurrentPlayer().getMinions().get(2) instanceof SearingTotem);
		
		assertEquals(cn0.data_.getCurrentPlayer().getMinions().get(0).getTotalHealth(), 2);
		assertEquals(cn0.data_.getCurrentPlayer().getMinions().get(1).getTotalHealth(), 7);
		assertEquals(cn0.data_.getCurrentPlayer().getMinions().get(2).getTotalHealth(), 1);
		assertEquals(cn0.data_.getWaitingPlayer().getMinions().get(0).getTotalHealth(), 2);
		assertEquals(cn0.data_.getWaitingPlayer().getMinions().get(1).getTotalHealth(), 7);

		assertEquals(cn0.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(cn0.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 7);
		assertEquals(cn0.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 2);
		assertEquals(cn0.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(cn0.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 7);
		
		HearthTreeNode cn1 = ret.getChildren().get(1);
		assertEquals(cn1.data_.getCurrentPlayer().getNumMinions(), 3);
		assertEquals(cn1.data_.getWaitingPlayer().getNumMinions(), 2);
		assertEquals(cn1.data_.getCurrentPlayer().getMana(), 6);
		assertEquals(cn1.data_.getWaitingPlayer().getMana(), 8);
		assertEquals(cn1.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(cn1.data_.getWaitingPlayerHero().getHealth(), 28);
		
		assertTrue(cn1.data_.getCurrentPlayer().getMinions().get(2) instanceof StoneclawTotem);
		
		assertEquals(cn1.data_.getCurrentPlayer().getMinions().get(0).getTotalHealth(), 2);
		assertEquals(cn1.data_.getCurrentPlayer().getMinions().get(1).getTotalHealth(), 7);
		assertEquals(cn1.data_.getCurrentPlayer().getMinions().get(2).getTotalHealth(), 2);
		assertEquals(cn1.data_.getWaitingPlayer().getMinions().get(0).getTotalHealth(), 2);
		assertEquals(cn1.data_.getWaitingPlayer().getMinions().get(1).getTotalHealth(), 7);

		assertEquals(cn1.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(cn1.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 7);
		assertEquals(cn1.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 1);
		assertEquals(cn1.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(cn1.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 7);
		
		HearthTreeNode cn2 = ret.getChildren().get(2);
		assertEquals(cn2.data_.getCurrentPlayer().getNumMinions(), 3);
		assertEquals(cn2.data_.getWaitingPlayer().getNumMinions(), 2);
		assertEquals(cn2.data_.getCurrentPlayer().getMana(), 6);
		assertEquals(cn2.data_.getWaitingPlayer().getMana(), 8);
		assertEquals(cn2.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(cn2.data_.getWaitingPlayerHero().getHealth(), 28);
		
		assertTrue(cn2.data_.getCurrentPlayer().getMinions().get(2) instanceof HealingTotem);
		
		assertEquals(cn2.data_.getCurrentPlayer().getMinions().get(0).getTotalHealth(), 2);
		assertEquals(cn2.data_.getCurrentPlayer().getMinions().get(1).getTotalHealth(), 7);
		assertEquals(cn2.data_.getCurrentPlayer().getMinions().get(2).getTotalHealth(), 2);
		assertEquals(cn2.data_.getWaitingPlayer().getMinions().get(0).getTotalHealth(), 2);
		assertEquals(cn2.data_.getWaitingPlayer().getMinions().get(1).getTotalHealth(), 7);

		assertEquals(cn2.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(cn2.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 7);
		assertEquals(cn2.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 1);
		assertEquals(cn2.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(cn2.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 7);
		
		HearthTreeNode cn3 = ret.getChildren().get(3);
		assertEquals(cn3.data_.getCurrentPlayer().getNumMinions(), 3);
		assertEquals(cn3.data_.getWaitingPlayer().getNumMinions(), 2);
		assertEquals(cn3.data_.getCurrentPlayer().getMana(), 6);
		assertEquals(cn3.data_.getWaitingPlayer().getMana(), 8);
		assertEquals(cn3.data_.getCurrentPlayerHero().getHealth(), 30);
		assertEquals(cn3.data_.getWaitingPlayerHero().getHealth(), 28);
		
		assertTrue(cn3.data_.getCurrentPlayer().getMinions().get(2) instanceof WrathOfAirTotem);
		
		assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(0).getTotalHealth(), 2);
		assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(1).getTotalHealth(), 7);
		assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(2).getTotalHealth(), 2);
		assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(0).getTotalHealth(), 2);
		assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(1).getTotalHealth(), 7);

		assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 7);
		assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 1);
		assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 2);
		assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 7);
		 

		RandomEffectNode rn = (RandomEffectNode)ret;
        ArtificialPlayer ai0 = ArtificialPlayer.buildStandardAI1();
		double score = rn.weightedAverageScore(deck, ai0);
		
		log.info("score = " + score);
	}
	


	@Test
	public void test1() throws HSException {

        ArtificialPlayer ai0 = ArtificialPlayer.buildStandardAI1();
		BoardModel resBoard = ai0.playTurn(0, board.data_, 200000000);

		assertEquals(resBoard.getCurrentPlayer().getMana(), 6);
		assertEquals(resBoard.getWaitingPlayer().getMana(), 8);
		assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getNumMinions(), 3);
		assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(resBoard).getNumMinions(), 1);

		log.info("{}",PlayerSide.CURRENT_PLAYER.getPlayer(resBoard).getMinions().get(2).getClass());
	}
}
