package com.hearthsim.test.heroes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.HealingTotem;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.minion.concrete.SearingTotem;
import com.hearthsim.card.minion.concrete.StoneclawTotem;
import com.hearthsim.card.minion.concrete.WrathOfAirTotem;
import com.hearthsim.card.minion.heroes.Shaman;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.card.spellcard.concrete.WildGrowth;
import com.hearthsim.exception.HSException;
import com.hearthsim.player.Player;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;

public class TestShaman {
	
	private HearthTreeNode board;
	private Deck deck;


	@Before
	public void setup() {
		board = new HearthTreeNode(new BoardState(new Shaman(), new Hero()));

		Minion minion0_0 = new BoulderfistOgre();
		Minion minion0_1 = new RaidLeader();
		Minion minion1_0 = new BoulderfistOgre();
		Minion minion1_1 = new RaidLeader();
		
		board.data_.placeCard_hand_p0(minion0_0);
		board.data_.placeCard_hand_p0(minion0_1);
				
		board.data_.placeCard_hand_p1(minion1_0);
		board.data_.placeCard_hand_p1(minion1_1);

		Card cards[] = new Card[30];
		for (int index = 0; index < 30; ++index) {
			cards[index] = new TheCoin();
		}
	
		deck = new Deck(cards);

		Card fb = new WildGrowth();
		board.data_.placeCard_hand_p0(fb);

		board.data_.setMana_p0((byte)8);
		board.data_.setMana_p1((byte)8);
		
		board.data_.setMaxMana_p0((byte)8);
		board.data_.setMaxMana_p1((byte)8);
		
		HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
		try {
			tmpBoard.data_.getCard_hand_p0(0).useOn(0, tmpBoard.data_.getHero_p0(), tmpBoard, deck, null);
			tmpBoard.data_.getCard_hand_p0(0).useOn(0, tmpBoard.data_.getHero_p0(), tmpBoard, deck, null);
		} catch (HSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
		try {
			board.data_.getCard_hand_p0(0).useOn(0, board.data_.getHero_p0(), board, deck, null);
			board.data_.getCard_hand_p0(0).useOn(0, board.data_.getHero_p0(), board, deck, null);
		} catch (HSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board.data_.resetMana();
		board.data_.resetMinions();
		
	}
	

	@Test
	public void test0() throws HSException {
		
		Minion target = board.data_.getCharacter(1, 0);
		Minion minion = board.data_.getMinion_p0(0);
		HearthTreeNode ret = minion.attack(1, target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getNumMinions_p0(), 2);
		assertEquals(board.data_.getNumMinions_p1(), 2);
		assertEquals(board.data_.getMana_p0(), 8);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 28);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 7);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), 7);

		assertEquals(board.data_.getMinion_p0(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p0(1).getTotalAttack(), 7);
		assertEquals(board.data_.getMinion_p1(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p1(1).getTotalAttack(), 7);
		
		
		target = board.data_.getCharacter(0, 0);
		Hero hero = board.data_.getHero_p0();
		ret = hero.useHeroAbility(0, target, board, deck, null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		
		assertTrue(ret instanceof RandomEffectNode);
		assertEquals(ret.getChildren().size(), 4);
		
		
		assertEquals(board.data_.getNumMinions_p0(), 2);
		assertEquals(board.data_.getNumMinions_p1(), 2);
		assertEquals(board.data_.getMana_p0(), 8);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 28);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 7);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), 7);

		assertEquals(board.data_.getMinion_p0(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p0(1).getTotalAttack(), 7);
		assertEquals(board.data_.getMinion_p1(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p1(1).getTotalAttack(), 7);

		HearthTreeNode cn0 = ret.getChildren().get(0);
		assertEquals(cn0.data_.getNumMinions_p0(), 3);
		assertEquals(cn0.data_.getNumMinions_p1(), 2);
		assertEquals(cn0.data_.getMana_p0(), 6);
		assertEquals(cn0.data_.getMana_p1(), 8);
		assertEquals(cn0.data_.getHero_p0().getHealth(), 30);
		assertEquals(cn0.data_.getHero_p1().getHealth(), 28);
		
		assertTrue(cn0.data_.getMinion_p0(2) instanceof SearingTotem);
		
		assertEquals(cn0.data_.getMinion_p0(0).getTotalHealth(), 2);
		assertEquals(cn0.data_.getMinion_p0(1).getTotalHealth(), 7);
		assertEquals(cn0.data_.getMinion_p0(2).getTotalHealth(), 1);
		assertEquals(cn0.data_.getMinion_p1(0).getTotalHealth(), 2);
		assertEquals(cn0.data_.getMinion_p1(1).getTotalHealth(), 7);

		assertEquals(cn0.data_.getMinion_p0(0).getTotalAttack(), 2);
		assertEquals(cn0.data_.getMinion_p0(1).getTotalAttack(), 7);
		assertEquals(cn0.data_.getMinion_p0(2).getTotalAttack(), 2);
		assertEquals(cn0.data_.getMinion_p1(0).getTotalAttack(), 2);
		assertEquals(cn0.data_.getMinion_p1(1).getTotalAttack(), 7);
		
		HearthTreeNode cn1 = ret.getChildren().get(1);
		assertEquals(cn1.data_.getNumMinions_p0(), 3);
		assertEquals(cn1.data_.getNumMinions_p1(), 2);
		assertEquals(cn1.data_.getMana_p0(), 6);
		assertEquals(cn1.data_.getMana_p1(), 8);
		assertEquals(cn1.data_.getHero_p0().getHealth(), 30);
		assertEquals(cn1.data_.getHero_p1().getHealth(), 28);
		
		assertTrue(cn1.data_.getMinion_p0(2) instanceof StoneclawTotem);
		
		assertEquals(cn1.data_.getMinion_p0(0).getTotalHealth(), 2);
		assertEquals(cn1.data_.getMinion_p0(1).getTotalHealth(), 7);
		assertEquals(cn1.data_.getMinion_p0(2).getTotalHealth(), 2);
		assertEquals(cn1.data_.getMinion_p1(0).getTotalHealth(), 2);
		assertEquals(cn1.data_.getMinion_p1(1).getTotalHealth(), 7);

		assertEquals(cn1.data_.getMinion_p0(0).getTotalAttack(), 2);
		assertEquals(cn1.data_.getMinion_p0(1).getTotalAttack(), 7);
		assertEquals(cn1.data_.getMinion_p0(2).getTotalAttack(), 1);
		assertEquals(cn1.data_.getMinion_p1(0).getTotalAttack(), 2);
		assertEquals(cn1.data_.getMinion_p1(1).getTotalAttack(), 7);
		
		HearthTreeNode cn2 = ret.getChildren().get(2);
		assertEquals(cn2.data_.getNumMinions_p0(), 3);
		assertEquals(cn2.data_.getNumMinions_p1(), 2);
		assertEquals(cn2.data_.getMana_p0(), 6);
		assertEquals(cn2.data_.getMana_p1(), 8);
		assertEquals(cn2.data_.getHero_p0().getHealth(), 30);
		assertEquals(cn2.data_.getHero_p1().getHealth(), 28);
		
		assertTrue(cn2.data_.getMinion_p0(2) instanceof HealingTotem);
		
		assertEquals(cn2.data_.getMinion_p0(0).getTotalHealth(), 2);
		assertEquals(cn2.data_.getMinion_p0(1).getTotalHealth(), 7);
		assertEquals(cn2.data_.getMinion_p0(2).getTotalHealth(), 2);
		assertEquals(cn2.data_.getMinion_p1(0).getTotalHealth(), 2);
		assertEquals(cn2.data_.getMinion_p1(1).getTotalHealth(), 7);

		assertEquals(cn2.data_.getMinion_p0(0).getTotalAttack(), 2);
		assertEquals(cn2.data_.getMinion_p0(1).getTotalAttack(), 7);
		assertEquals(cn2.data_.getMinion_p0(2).getTotalAttack(), 1);
		assertEquals(cn2.data_.getMinion_p1(0).getTotalAttack(), 2);
		assertEquals(cn2.data_.getMinion_p1(1).getTotalAttack(), 7);
		
		HearthTreeNode cn3 = ret.getChildren().get(3);
		assertEquals(cn3.data_.getNumMinions_p0(), 3);
		assertEquals(cn3.data_.getNumMinions_p1(), 2);
		assertEquals(cn3.data_.getMana_p0(), 6);
		assertEquals(cn3.data_.getMana_p1(), 8);
		assertEquals(cn3.data_.getHero_p0().getHealth(), 30);
		assertEquals(cn3.data_.getHero_p1().getHealth(), 28);
		
		assertTrue(cn3.data_.getMinion_p0(2) instanceof WrathOfAirTotem);
		
		assertEquals(cn3.data_.getMinion_p0(0).getTotalHealth(), 2);
		assertEquals(cn3.data_.getMinion_p0(1).getTotalHealth(), 7);
		assertEquals(cn3.data_.getMinion_p0(2).getTotalHealth(), 2);
		assertEquals(cn3.data_.getMinion_p1(0).getTotalHealth(), 2);
		assertEquals(cn3.data_.getMinion_p1(1).getTotalHealth(), 7);

		assertEquals(cn3.data_.getMinion_p0(0).getTotalAttack(), 2);
		assertEquals(cn3.data_.getMinion_p0(1).getTotalAttack(), 7);
		assertEquals(cn3.data_.getMinion_p0(2).getTotalAttack(), 1);
		assertEquals(cn3.data_.getMinion_p1(0).getTotalAttack(), 2);
		assertEquals(cn3.data_.getMinion_p1(1).getTotalAttack(), 7);
		 
		ArtificialPlayer ai0 = new ArtificialPlayer(
				0.9,
				0.9,
				1.0,
				1.0,
				1.0,
				0.1,
				0.1,
				0.1,
				0.5,
				0.5,
				0.0,
				0.5,
				0.0,
				0.0
				);
		
		RandomEffectNode rn = (RandomEffectNode)ret;
		double score = rn.weightedAverageScore(deck, ai0);
		
		System.out.println("score = " + score);
	}
	


	@Test
	public void test1() throws HSException {
		 
		ArtificialPlayer ai0 = new ArtificialPlayer(
				0.9,
				0.9,
				1.0,
				1.0,
				1.0,
				0.1,
				0.1,
				0.1,
				0.5,
				0.5,
				0.0,
				0.5,
				0.0,
				0.0
				);
		
		Player player0 = new Player("player0", new Shaman(), deck);
		Player player1 = new Player("player1", new Hero(), deck);
		BoardState resBoard = ai0.playTurn(0, board.data_, player0, player1, 200000000);

		assertEquals(resBoard.getMana_p0(), 6);
		assertEquals(resBoard.getMana_p1(), 8);
		assertEquals(resBoard.getNumMinions_p0(), 3);
		assertEquals(resBoard.getNumMinions_p1(), 1);

		System.out.println(resBoard.getMinion_p0(2).getClass());
	}
}
