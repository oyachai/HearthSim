package com.hearthsim.test.minion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

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
import com.hearthsim.util.BoardState;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestArmorsmith {

	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() {
		board = new HearthTreeNode(new BoardState());

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
		
		Minion fb = new Armorsmith();
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
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getTotalHealth(), 5);
		assertEquals(board.data_.getMinion_p0(1).getTotalHealth(), 3);
		assertEquals(board.data_.getMinion_p0(2).getTotalHealth(), 6);
		assertEquals(board.data_.getMinion_p1(0).getTotalHealth(), 2);
		assertEquals(board.data_.getMinion_p1(1).getTotalHealth(), 2);
		
		assertEquals(board.data_.getMinion_p0(0).getTotalAttack(), 3);
		assertEquals(board.data_.getMinion_p0(1).getTotalAttack(), 3);
		assertEquals(board.data_.getMinion_p0(2).getTotalAttack(), 7);
		assertEquals(board.data_.getMinion_p1(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p1(1).getTotalAttack(), 4);
		
		assertEquals(board.data_.getMinion_p0(0).getAuraAttack(), 2);
		assertEquals(board.data_.getMinion_p0(1).getAuraAttack(), 1);
		assertEquals(board.data_.getMinion_p0(2).getAuraAttack(), 1);
		assertEquals(board.data_.getMinion_p1(0).getAuraAttack(), 0);
		assertEquals(board.data_.getMinion_p1(1).getAuraAttack(), 1);

		
		
		
		
		board.data_.placeCard_hand_p0(new HolySmite());
		theCard = board.data_.getCard_hand_p0(0);
		
		target = board.data_.getCharacter(0, 3);
		ret = theCard.useOn(0, target, ret, deck, null);
		assertFalse(ret == null);

		assertEquals(ret.data_.getNumCards_hand(), 0);
		assertEquals(ret.data_.getNumMinions_p0(), 3);
		assertEquals(ret.data_.getNumMinions_p1(), 2);
		assertEquals(ret.data_.getHero_p0().getHealth(), 30);
		assertEquals(ret.data_.getHero_p1().getHealth(), 30);
		assertEquals(ret.data_.getHero_p0().getArmor(), 1);
		assertEquals(ret.data_.getHero_p1().getArmor(), 0);

		assertEquals(ret.data_.getMinion_p0(0).getTotalHealth(), 5);
		assertEquals(ret.data_.getMinion_p0(1).getTotalHealth(), 3);
		assertEquals(ret.data_.getMinion_p0(2).getTotalHealth(), 4);
		assertEquals(ret.data_.getMinion_p1(0).getTotalHealth(), 2);
		assertEquals(ret.data_.getMinion_p1(1).getTotalHealth(), 2);
		
		assertEquals(ret.data_.getMinion_p0(0).getTotalAttack(), 3);
		assertEquals(ret.data_.getMinion_p0(1).getTotalAttack(), 3);
		assertEquals(ret.data_.getMinion_p0(2).getTotalAttack(), 7);
		assertEquals(ret.data_.getMinion_p1(0).getTotalAttack(), 2);
		assertEquals(ret.data_.getMinion_p1(1).getTotalAttack(), 4);
		
		assertEquals(ret.data_.getMinion_p0(0).getAuraAttack(), 2);
		assertEquals(ret.data_.getMinion_p0(1).getAuraAttack(), 1);
		assertEquals(ret.data_.getMinion_p0(2).getAuraAttack(), 1);
		assertEquals(ret.data_.getMinion_p1(0).getAuraAttack(), 0);
		assertEquals(ret.data_.getMinion_p1(1).getAuraAttack(), 1);

		
		
		
		board.data_.placeCard_hand_p0(new HolySmite());
		theCard = board.data_.getCard_hand_p0(0);
		target = board.data_.getCharacter(1, 2);
		ret = theCard.useOn(1, target, ret, deck, null);
		assertFalse(ret == null);
		assertEquals(ret.data_.getNumCards_hand(), 0);
		assertEquals(ret.data_.getNumMinions_p0(), 3);
		assertEquals(ret.data_.getNumMinions_p1(), 1);
		assertEquals(ret.data_.getHero_p0().getHealth(), 30);
		assertEquals(ret.data_.getHero_p1().getHealth(), 30);
		assertEquals(ret.data_.getHero_p0().getArmor(), 1);
		assertEquals(ret.data_.getHero_p1().getArmor(), 0);

		assertEquals(ret.data_.getMinion_p0(0).getTotalHealth(), 5);
		assertEquals(ret.data_.getMinion_p0(1).getTotalHealth(), 3);
		assertEquals(ret.data_.getMinion_p0(2).getTotalHealth(), 4);
		assertEquals(ret.data_.getMinion_p1(0).getTotalHealth(), 2);
		
		assertEquals(ret.data_.getMinion_p0(0).getTotalAttack(), 3);
		assertEquals(ret.data_.getMinion_p0(1).getTotalAttack(), 3);
		assertEquals(ret.data_.getMinion_p0(2).getTotalAttack(), 7);
		assertEquals(ret.data_.getMinion_p1(0).getTotalAttack(), 2);
		
		assertEquals(ret.data_.getMinion_p0(0).getAuraAttack(), 2);
		assertEquals(ret.data_.getMinion_p0(1).getAuraAttack(), 1);
		assertEquals(ret.data_.getMinion_p0(2).getAuraAttack(), 1);
		assertEquals(ret.data_.getMinion_p1(0).getAuraAttack(), 0);
		
		
		ret = new HearthTreeNode(ret.data_.flipPlayers());
		ret.data_.placeCard_hand_p0(new HolySmite());
		theCard = ret.data_.getCard_hand_p0(0);
		target = ret.data_.getCharacter(1, 2);
		ret = theCard.useOn(1, target, ret, deck, null);
		assertFalse(ret == null);
		assertEquals(ret.data_.getNumCards_hand(), 0);
		assertEquals(ret.data_.getNumMinions_p1(), 3);
		assertEquals(ret.data_.getNumMinions_p0(), 1);
		assertEquals(ret.data_.getHero_p1().getHealth(), 30);
		assertEquals(ret.data_.getHero_p0().getHealth(), 30);
		assertEquals(ret.data_.getHero_p1().getArmor(), 2);
		assertEquals(ret.data_.getHero_p0().getArmor(), 0);

		assertEquals(ret.data_.getMinion_p1(0).getTotalHealth(), 5);
		assertEquals(ret.data_.getMinion_p1(1).getTotalHealth(), 1);
		assertEquals(ret.data_.getMinion_p1(2).getTotalHealth(), 4);
		assertEquals(ret.data_.getMinion_p0(0).getTotalHealth(), 2);
		
		assertEquals(ret.data_.getMinion_p1(0).getTotalAttack(), 3);
		assertEquals(ret.data_.getMinion_p1(1).getTotalAttack(), 3);
		assertEquals(ret.data_.getMinion_p1(2).getTotalAttack(), 7);
		assertEquals(ret.data_.getMinion_p0(0).getTotalAttack(), 2);
		
		assertEquals(ret.data_.getMinion_p1(0).getAuraAttack(), 2);
		assertEquals(ret.data_.getMinion_p1(1).getAuraAttack(), 1);
		assertEquals(ret.data_.getMinion_p1(2).getAuraAttack(), 1);
		assertEquals(ret.data_.getMinion_p0(0).getAuraAttack(), 0);
	}
	
}
