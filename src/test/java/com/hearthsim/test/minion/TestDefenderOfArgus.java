package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.DefenderOfArgus;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.minion.concrete.StormwindChampion;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestDefenderOfArgus {


	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() {
		board = new HearthTreeNode(new BoardModel());

		Minion minion0_0 = new StormwindChampion();
		Minion minion0_1 = new RaidLeader();
		Minion minion1_0 = new BloodfenRaptor();
		Minion minion1_1 = new RaidLeader();
		
		board.data_.placeCard_hand_p0(minion0_0);
		board.data_.placeCard_hand_p0(minion0_1);
				
		board.data_.placeCard_hand_p1(minion1_0);
		board.data_.placeCard_hand_p1(minion1_1);

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
		
		Minion fb = new DefenderOfArgus();
		board.data_.placeCard_hand_p0(fb);

	}
	

	@Test
	public void test0() throws HSException {

		Minion target = board.data_.getCharacter(0, 0);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(0, target, board, deck, null);
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getNumMinions_p0(), 3);
		assertEquals(board.data_.getNumMinions_p1(), 2);
		assertEquals(board.data_.getMana_p0(), 6);
		assertEquals(board.data_.getMana_p1(), 10);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getTotalHealth(), 4);
		assertEquals(board.data_.getMinion_p0(1).getTotalHealth(), 4);
		assertEquals(board.data_.getMinion_p0(2).getTotalHealth(), 6);
		assertEquals(board.data_.getMinion_p1(0).getTotalHealth(), 2);
		assertEquals(board.data_.getMinion_p1(1).getTotalHealth(), 2);
		
		assertEquals(board.data_.getMinion_p0(0).getTotalAttack(), 4);
		assertEquals(board.data_.getMinion_p0(1).getTotalAttack(), 4);
		assertEquals(board.data_.getMinion_p0(2).getTotalAttack(), 7);
		assertEquals(board.data_.getMinion_p1(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p1(1).getTotalAttack(), 4);
		
		assertEquals(board.data_.getMinion_p0(0).getAuraAttack(), 2);
		assertEquals(board.data_.getMinion_p0(1).getAuraAttack(), 1);
		assertEquals(board.data_.getMinion_p0(2).getAuraAttack(), 1);
		assertEquals(board.data_.getMinion_p1(0).getAuraAttack(), 0);
		assertEquals(board.data_.getMinion_p1(1).getAuraAttack(), 1);
		
		assertFalse(board.data_.getMinion_p0(0).getTaunt());
		assertTrue(board.data_.getMinion_p0(1).getTaunt());
		assertFalse(board.data_.getMinion_p0(2).getTaunt());
	}
	
	@Test
	public void test1() throws HSException {

		Minion target = board.data_.getCharacter(0, 1);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(0, target, board, deck, null);
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getNumMinions_p0(), 3);
		assertEquals(board.data_.getNumMinions_p1(), 2);
		assertEquals(board.data_.getMana_p0(), 6);
		assertEquals(board.data_.getMana_p1(), 10);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getTotalHealth(), 4);
		assertEquals(board.data_.getMinion_p0(1).getTotalHealth(), 4);
		assertEquals(board.data_.getMinion_p0(2).getTotalHealth(), 7);
		assertEquals(board.data_.getMinion_p1(0).getTotalHealth(), 2);
		assertEquals(board.data_.getMinion_p1(1).getTotalHealth(), 2);
		
		assertEquals(board.data_.getMinion_p0(0).getTotalAttack(), 4);
		assertEquals(board.data_.getMinion_p0(1).getTotalAttack(), 4);
		assertEquals(board.data_.getMinion_p0(2).getTotalAttack(), 8);
		assertEquals(board.data_.getMinion_p1(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p1(1).getTotalAttack(), 4);
		
		assertEquals(board.data_.getMinion_p0(0).getAuraAttack(), 1);
		assertEquals(board.data_.getMinion_p0(1).getAuraAttack(), 2);
		assertEquals(board.data_.getMinion_p0(2).getAuraAttack(), 1);
		assertEquals(board.data_.getMinion_p1(0).getAuraAttack(), 0);
		assertEquals(board.data_.getMinion_p1(1).getAuraAttack(), 1);
		
		assertTrue(board.data_.getMinion_p0(0).getTaunt());
		assertFalse(board.data_.getMinion_p0(1).getTaunt());
		assertTrue(board.data_.getMinion_p0(2).getTaunt());
	}
}
