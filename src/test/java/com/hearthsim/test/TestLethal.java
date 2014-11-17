package com.hearthsim.test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.concrete.BluegillWarrior;
import com.hearthsim.card.minion.concrete.Boar;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.ChillwindYeti;
import com.hearthsim.card.minion.concrete.GoldshireFootman;
import com.hearthsim.card.minion.concrete.KoboldGeomancer;
import com.hearthsim.card.minion.concrete.RiverCrocolisk;
import com.hearthsim.card.minion.concrete.SenjinShieldmasta;
import com.hearthsim.card.minion.concrete.TimberWolf;
import com.hearthsim.card.spellcard.concrete.Assassinate;
import com.hearthsim.card.spellcard.concrete.Frostbolt;
import com.hearthsim.card.spellcard.concrete.HolySmite;
import com.hearthsim.card.spellcard.concrete.Innervate;
import com.hearthsim.card.spellcard.concrete.MindControl;
import com.hearthsim.card.spellcard.concrete.Sap;
import com.hearthsim.card.spellcard.concrete.Silence;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestLethal {

	public Deck deck0;
	public Deck deck1;

	BoardStateFactoryBase factory;
	public BoardModel startingBoard;
	HearthTreeNode root;

	Hero ownHero;
	Hero enemyHero;

	@Before
	public void setup() {
		Card cards[] = new Card[10];
		for(int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}

		deck0 = new Deck(cards);
		deck1 = deck0.deepCopy();

		factory = new BoardStateFactoryBase(this.deck0, this.deck1);
		startingBoard = new BoardModel();
		startingBoard.getCurrentPlayer().addMana(10);
		startingBoard.getCurrentPlayer().addMaxMana(10);
		
		root = new HearthTreeNode(startingBoard);
		this.ownHero = startingBoard.getCurrentPlayerHero();
		this.enemyHero = startingBoard.getWaitingPlayerHero();
	}

	@Test
	public void testMinionAttack() throws HSException {
		this.enemyHero.setHealth((byte)4);
		this.startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new ChillwindYeti());

		this.factory.addChildLayers(this.root, 1);
		assertTrue(this.hasLethalAtDepth(this.root, 1));
	}

	@Test
	public void testMinionAttackMultiple() throws HSException {
		this.enemyHero.setHealth((byte)8);
		this.startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new ChillwindYeti());
		this.startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new ChillwindYeti());

		this.factory.addChildLayers(this.root, 2);
		assertTrue(this.hasLethalAtDepth(this.root, 2));
	}

	@Test
	public void testSpell() throws HSException {
		this.enemyHero.setHealth((byte)1);
		this.startingBoard.placeCardHand(PlayerSide.CURRENT_PLAYER, new HolySmite());

		this.factory.addChildLayers(this.root, 1);
		assertTrue(this.hasLethalAtDepth(this.root, 1));
	}

	@Test
	public void testSpellMultiple() throws HSException {
		this.enemyHero.setHealth((byte)3);
		this.startingBoard.placeCardHand(PlayerSide.CURRENT_PLAYER, new HolySmite());
		this.startingBoard.placeCardHand(PlayerSide.CURRENT_PLAYER, new HolySmite());

		this.factory.addChildLayers(this.root, 2);
		assertTrue(this.hasLethalAtDepth(this.root, 2));
	}

	@Test
	public void testKoboldThenSmite() throws HSException {
		this.enemyHero.setHealth((byte)3);
		this.startingBoard.placeCardHand(PlayerSide.CURRENT_PLAYER, new KoboldGeomancer());
		this.startingBoard.placeCardHand(PlayerSide.CURRENT_PLAYER, new HolySmite());

		this.factory.addChildLayers(this.root, 2);
		assertTrue(this.hasLethalAtDepth(this.root, 2));
	}

	@Test
	public void testInnervateSmiteSmite() throws HSException {
		this.startingBoard.getCurrentPlayer().setMana(0);
		this.enemyHero.setHealth((byte)3);
		this.startingBoard.placeCardHand(PlayerSide.CURRENT_PLAYER, new HolySmite());
		this.startingBoard.placeCardHand(PlayerSide.CURRENT_PLAYER, new Innervate());
		this.startingBoard.placeCardHand(PlayerSide.CURRENT_PLAYER, new HolySmite());

		this.factory.addChildLayers(this.root, 3);
		assertTrue(this.hasLethalAtDepth(this.root, 3));
	}

	@Test
	public void testMinionAttackCharge() throws HSException {
		this.enemyHero.setHealth((byte)6);
		this.startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new ChillwindYeti());
		this.startingBoard.placeCardHand(PlayerSide.CURRENT_PLAYER, new BluegillWarrior());

		this.factory.addChildLayers(this.root, 3);
		assertTrue(this.hasLethalAtDepth(this.root, 3));
	}

	@Test
	public void testMinionWithBuff() throws HSException {
		this.enemyHero.setHealth((byte)3);
		this.startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new RiverCrocolisk());
		this.startingBoard.placeCardHand(PlayerSide.CURRENT_PLAYER, new Boar());
		this.startingBoard.placeCardHand(PlayerSide.CURRENT_PLAYER, new TimberWolf());

		this.factory.addChildLayers(this.root, 3);
		assertTrue(this.hasLethalAtDepth(this.root, 3));
	}

	@Test
	public void testRemoveTauntAndAttack() throws HSException {
		this.removeTauntTest(new Assassinate());
	}

	@Test
	public void testSilenceTauntAndAttack() throws HSException {
		this.removeTauntTest(new Silence());
	}

	@Test
	public void testSapTauntAndAttack() throws HSException {
		this.removeTauntTest(new Sap());
	}

	@Test
	public void testMindControlTauntAndAttack() throws HSException {
		this.removeTauntTest(new MindControl());
	}
	
	@Test
	public void testKillTauntWithSmallerAttack() throws HSException {
		this.enemyHero.setHealth((byte)6);
		this.startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new GoldshireFootman());

		this.startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new BoulderfistOgre());
		this.startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new ChillwindYeti());

		this.factory.addChildLayers(this.root, 2);
		assertTrue(this.hasLethalAtDepth(this.root, 2));
	}

	@Test
	public void testComplicatedTauntMath() throws HSException {
		this.enemyHero.setHealth((byte)6);
		this.startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new GoldshireFootman());
		this.startingBoard.placeCardHand(PlayerSide.CURRENT_PLAYER, new BluegillWarrior()); // +2 = 2

		this.startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new SenjinShieldmasta());
		this.startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new RiverCrocolisk()); // +1 = 3
		this.startingBoard.placeCardHand(PlayerSide.CURRENT_PLAYER, new Frostbolt()); // +1 = 4

		this.startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new BoulderfistOgre()); // +1 = 5

		this.factory.addChildLayers(this.root, 5);
		assertTrue(this.hasLethalAtDepth(this.root, 5));
		assertTrue(this.hasLethalAtDepth(this.root, 5));
	}

	private void removeTauntTest(Card removal) throws HSException {
		this.enemyHero.setHealth((byte)4);
		this.startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new SenjinShieldmasta());

		this.startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new ChillwindYeti());
		this.startingBoard.placeCardHand(PlayerSide.CURRENT_PLAYER, removal);

		this.factory.addChildLayers(this.root, 2);
		assertTrue(this.hasLethalAtDepth(this.root, 2));
	}

	private boolean hasLethalAtDepth(HearthTreeNode node, int depth) {
		if(depth < 0) {
			return false;
		}
		if(depth == 0) {
			return node.data_.isLethalState();
		}
		if(node.isLeaf()) {
			return false;
		}
		for(HearthTreeNode child : node.getChildren()) {
			if(this.hasLethalAtDepth(child, depth - 1)) {
				return true;
			}
		}

		return false;
	}
}
