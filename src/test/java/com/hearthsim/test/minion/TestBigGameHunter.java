package com.hearthsim.test.minion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.Abomination;
import com.hearthsim.card.minion.concrete.BigGameHunter;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.HarvestGolem;
import com.hearthsim.card.minion.concrete.LootHoarder;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.minion.concrete.StormwindChampion;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.boardstate.BoardState;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestBigGameHunter {

	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() {
		board = new HearthTreeNode(new BoardState());

		Minion minion0_0 = new StormwindChampion();
		Minion minion0_1 = new RaidLeader();
		Minion minion0_2 = new HarvestGolem();
	
		Minion minion1_0 = new BoulderfistOgre();
		Minion minion1_1 = new RaidLeader();
		Minion minion1_2 = new Abomination();
		Minion minion1_3 = new LootHoarder();
		
		board.data_.placeCard_hand_p0(minion0_0);
		board.data_.placeCard_hand_p0(minion0_1);
		board.data_.placeCard_hand_p0(minion0_2);
				
		board.data_.placeCard_hand_p1(minion1_0);
		board.data_.placeCard_hand_p1(minion1_1);
		board.data_.placeCard_hand_p1(minion1_2);
		board.data_.placeCard_hand_p1(minion1_3);

		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}
	
		deck = new Deck(cards);

		board.data_.setMana_p0((byte)10);
		board.data_.setMana_p1((byte)10);
		
		board.data_.setMaxMana_p0((byte)10);
		board.data_.setMaxMana_p1((byte)10);
		
		HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
		try {
			tmpBoard.data_.getCard_hand_p0(0).useOn(0, tmpBoard.data_.getHero_p0(), tmpBoard, deck, null);
			tmpBoard.data_.getCard_hand_p0(0).useOn(0, tmpBoard.data_.getHero_p0(), tmpBoard, deck, null);
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
			board.data_.getCard_hand_p0(0).useOn(0, board.data_.getHero_p0(), board, deck, null);
		} catch (HSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board.data_.resetMana();
		board.data_.resetMinions();
		
		Minion fb = new BigGameHunter();
		board.data_.placeCard_hand_p0(fb);

	}
	
	
	
	@Test
	public void test0() throws HSException {
		
		//null case
		Minion target = board.data_.getCharacter(1, 0);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(1, target, board, deck, deck);
		
		assertTrue(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getNumMinions_p0(), 3);
		assertEquals(board.data_.getNumMinions_p1(), 4);
		assertEquals(board.data_.getMana_p0(), 10);
		assertEquals(board.data_.getMana_p1(), 10);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		
		assertEquals(board.data_.getMinion_p0(0).getTotalHealth(), 4);
		assertEquals(board.data_.getMinion_p0(1).getTotalHealth(), 3);
		assertEquals(board.data_.getMinion_p0(2).getTotalHealth(), 6);
		
		assertEquals(board.data_.getMinion_p1(0).getTotalHealth(), 1);
		assertEquals(board.data_.getMinion_p1(1).getTotalHealth(), 4);
		assertEquals(board.data_.getMinion_p1(2).getTotalHealth(), 2);
		assertEquals(board.data_.getMinion_p1(3).getTotalHealth(), 7);

		assertEquals(board.data_.getMinion_p0(0).getTotalAttack(), 4);
		assertEquals(board.data_.getMinion_p0(1).getTotalAttack(), 3);
		assertEquals(board.data_.getMinion_p0(2).getTotalAttack(), 7);
		
		assertEquals(board.data_.getMinion_p1(0).getTotalAttack(), 3);
		assertEquals(board.data_.getMinion_p1(1).getTotalAttack(), 5);
		assertEquals(board.data_.getMinion_p1(2).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p1(3).getTotalAttack(), 7);
	}
	
	@Test
	public void test1() throws HSException {	
		
		//set the remaining total health of Abomination to 1
		board.data_.getMinion_p1(1).setHealth((byte)1);
		
		//And the Abomination was somehow buffed to 8 attack
		board.data_.getMinion_p1(1).setAttack((byte)8);
		
		//null case
		Minion target = board.data_.getCharacter(0, 0);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(0, target, board, deck, deck);
		
		assertFalse(ret == null);
		assertEquals(ret.data_.getNumCards_hand(), 0);
		assertEquals(ret.data_.getNumMinions_p0(), 4);
		assertEquals(ret.data_.getNumMinions_p1(), 4);
		assertEquals(ret.data_.getMana_p0(), 6);
		assertEquals(ret.data_.getMana_p1(), 10);
		assertEquals(ret.data_.getHero_p0().getHealth(), 30);
		assertEquals(ret.data_.getHero_p1().getHealth(), 30);
		
		assertEquals(ret.data_.getMinion_p0(0).getTotalHealth(), 3);
		assertEquals(ret.data_.getMinion_p0(1).getTotalHealth(), 4);
		assertEquals(ret.data_.getMinion_p0(2).getTotalHealth(), 3);
		assertEquals(ret.data_.getMinion_p0(3).getTotalHealth(), 6);
		
		assertEquals(ret.data_.getMinion_p1(0).getTotalHealth(), 1);
		assertEquals(ret.data_.getMinion_p1(1).getTotalHealth(), 1);
		assertEquals(ret.data_.getMinion_p1(2).getTotalHealth(), 2);
		assertEquals(ret.data_.getMinion_p1(3).getTotalHealth(), 7);

		assertEquals(ret.data_.getMinion_p0(0).getTotalAttack(), 6);
		assertEquals(ret.data_.getMinion_p0(1).getTotalAttack(), 4);
		assertEquals(ret.data_.getMinion_p0(2).getTotalAttack(), 3);
		assertEquals(ret.data_.getMinion_p0(3).getTotalAttack(), 7);
		
		assertEquals(ret.data_.getMinion_p1(0).getTotalAttack(), 3);
		assertEquals(ret.data_.getMinion_p1(1).getTotalAttack(), 9);
		assertEquals(ret.data_.getMinion_p1(2).getTotalAttack(), 2);
		assertEquals(ret.data_.getMinion_p1(3).getTotalAttack(), 7);
		
		//3 children nodes: Kill the Stormwind Champion, Boulderfist Ogre, and Abomination
		assertEquals(ret.numChildren(), 3);
		
		//First child node is the one that kills the Stormwind Champion
		HearthTreeNode cn3 = ret.getChildren().get(0);
		assertEquals(cn3.data_.getNumCards_hand_p0(), 0);
		assertEquals(cn3.data_.getNumCards_hand_p1(), 0);
		assertEquals(cn3.data_.getNumMinions_p0(), 3);
		assertEquals(cn3.data_.getNumMinions_p1(), 4);
		assertEquals(cn3.data_.getMana_p0(), 6);
		assertEquals(cn3.data_.getMana_p1(), 10);
		assertEquals(cn3.data_.getHero_p0().getHealth(), 30);
		assertEquals(cn3.data_.getHero_p1().getHealth(), 30);
		
		assertEquals(cn3.data_.getMinion_p0(0).getTotalHealth(), 2);
		assertEquals(cn3.data_.getMinion_p0(1).getTotalHealth(), 3);
		assertEquals(cn3.data_.getMinion_p0(2).getTotalHealth(), 2);

		assertEquals(cn3.data_.getMinion_p1(0).getTotalHealth(), 1);
		assertEquals(cn3.data_.getMinion_p1(1).getTotalHealth(), 1);
		assertEquals(cn3.data_.getMinion_p1(2).getTotalHealth(), 2);
		assertEquals(ret.data_.getMinion_p1(3).getTotalHealth(), 7);

		assertEquals(cn3.data_.getMinion_p0(0).getTotalAttack(), 5);
		assertEquals(cn3.data_.getMinion_p0(1).getTotalAttack(), 3);
		assertEquals(cn3.data_.getMinion_p0(2).getTotalAttack(), 2);
		
		assertEquals(ret.data_.getMinion_p1(0).getTotalAttack(), 3);
		assertEquals(cn3.data_.getMinion_p1(1).getTotalAttack(), 9);
		assertEquals(cn3.data_.getMinion_p1(2).getTotalAttack(), 2);
		assertEquals(cn3.data_.getMinion_p1(3).getTotalAttack(), 7);
	
		//Second child node is the one that kills the Abomination
		HearthTreeNode cn4 = ret.getChildren().get(1);
		assertEquals(cn4.data_.getNumCards_hand_p0(), 0);
		assertEquals(cn4.data_.getNumCards_hand_p1(), 1);
		assertEquals(cn4.data_.getNumMinions_p0(), 4);
		assertEquals(cn4.data_.getNumMinions_p1(), 1);
		assertEquals(cn4.data_.getMana_p0(), 6);
		assertEquals(cn4.data_.getMana_p1(), 10);
		assertEquals(cn4.data_.getHero_p0().getHealth(), 28);
		assertEquals(cn4.data_.getHero_p1().getHealth(), 28);
		
		assertEquals(cn4.data_.getMinion_p0(0).getTotalHealth(), 1);
		assertEquals(cn4.data_.getMinion_p0(1).getTotalHealth(), 2);
		assertEquals(cn4.data_.getMinion_p0(2).getTotalHealth(), 1);
		assertEquals(cn4.data_.getMinion_p0(3).getTotalHealth(), 4);
		assertEquals(cn4.data_.getMinion_p1(0).getTotalHealth(), 5);

		assertEquals(cn4.data_.getMinion_p0(0).getTotalAttack(), 6);
		assertEquals(cn4.data_.getMinion_p0(1).getTotalAttack(), 4);
		assertEquals(cn4.data_.getMinion_p0(2).getTotalAttack(), 3);
		assertEquals(cn4.data_.getMinion_p0(3).getTotalAttack(), 7);
		assertEquals(cn4.data_.getMinion_p1(0).getTotalAttack(), 6);
	}
}
