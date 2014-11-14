package com.hearthsim.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.ArgentSquire;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.Boar;
import com.hearthsim.card.minion.concrete.RiverCrocolisk;
import com.hearthsim.card.minion.heroes.Mage;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.card.spellcard.concrete.Claw;
import com.hearthsim.card.spellcard.concrete.HolySmite;
import com.hearthsim.card.spellcard.concrete.ShadowBolt;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestBoardStateFactoryBase {

	public Deck deck0;
	public Deck deck1;

	@Before
	public void setup() {
		Card cards[] = new Card[10];
		for(int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}

		deck0 = new Deck(cards);
		deck1 = deck0.deepCopy();
	}

	@Test
	public void testMinionPlacementSingle() throws HSException {
		BoardModel startingBoard = new BoardModel();
		PlayerModel firstPlayer = startingBoard.getCurrentPlayer();
		firstPlayer.addMana(2);
		firstPlayer.addMaxMana(2);
		firstPlayer.placeCardHand(new BloodfenRaptor());

		BoardModel expectedBoard = new BoardModel();
		Minion expectedMinion = new BloodfenRaptor();
		expectedMinion.hasBeenUsed(true); // we are checking state immediately after being played
		expectedMinion.hasAttacked(true);
		expectedBoard.placeMinion(PlayerSide.CURRENT_PLAYER, expectedMinion);
		expectedBoard.getCurrentPlayer().setMana(0);
		expectedBoard.getCurrentPlayer().setMaxMana(2);

		BoardStateFactoryBase factory = new BoardStateFactoryBase(this.deck0, this.deck1);
		HearthTreeNode root = new HearthTreeNode(startingBoard);

		ArrayList<HearthTreeNode> actuals = factory.getNextLayerOfCardBranches(root);
		assertEquals(2, actuals.size());
		assertNodeListContainsBoardModel(actuals, expectedBoard);
	}

	@Test
	public void testMinionPlacementMultiple() throws HSException {
		BoardModel startingBoard = new BoardModel();
		startingBoard.getCurrentPlayer().addMana(2);
		startingBoard.getCurrentPlayer().addMaxMana(2);
		startingBoard.getCurrentPlayer().placeCardHand(new BloodfenRaptor());
		startingBoard.getCurrentPlayer().placeCardHand(new ArgentSquire());

		BoardStateFactoryBase factory = new BoardStateFactoryBase(this.deck0, this.deck1);
		HearthTreeNode root = new HearthTreeNode(startingBoard);
		ArrayList<HearthTreeNode> actuals = factory.getNextLayerOfCardBranches(root);
		assertEquals(3, actuals.size());

		BoardModel expectedBoard = new BoardModel();
		Minion expectedMinion = new BloodfenRaptor();
		expectedMinion.hasBeenUsed(true); // we are checking state immediately after being played
		expectedMinion.hasAttacked(true);
		expectedBoard.placeMinion(PlayerSide.CURRENT_PLAYER, expectedMinion);

		expectedBoard.getCurrentPlayer().placeCardHand(new ArgentSquire());
		expectedBoard.getCurrentPlayer().setMana(0);
		expectedBoard.getCurrentPlayer().setMaxMana(2);
		assertNodeListContainsBoardModel(actuals, expectedBoard, 1);

		expectedBoard = new BoardModel();
		expectedMinion = new ArgentSquire();
		expectedMinion.hasBeenUsed(true); // we are checking state immediately after being played
		expectedMinion.hasAttacked(true);
		expectedBoard.placeMinion(PlayerSide.CURRENT_PLAYER, expectedMinion);

		expectedBoard.getCurrentPlayer().placeCardHand(new BloodfenRaptor());
		expectedBoard.getCurrentPlayer().setMana(1);
		expectedBoard.getCurrentPlayer().setMaxMana(2);
		assertNodeListContainsBoardModel(actuals, expectedBoard, 0);
	}

	@Test
	public void testMinionPlacementNotEnoughMana() throws HSException {
		BoardModel startingBoard = new BoardModel();
		PlayerModel firstPlayer = startingBoard.getCurrentPlayer();
		firstPlayer.addMana(1);
		firstPlayer.addMaxMana(1);
		firstPlayer.placeCardHand(new BloodfenRaptor());

		BoardStateFactoryBase factory = new BoardStateFactoryBase(this.deck0, this.deck1);
		HearthTreeNode root = new HearthTreeNode(startingBoard);

		ArrayList<HearthTreeNode> actuals = factory.getNextLayerOfCardBranches(root);
		assertEquals(0, actuals.size());
	}

	@Test
	public void testMinionPlacementPositioning() throws HSException {
		BoardModel startingBoard = new BoardModel();
		startingBoard.getCurrentPlayer().addMana(2);
		startingBoard.getCurrentPlayer().addMaxMana(2);
		startingBoard.getCurrentPlayer().placeCardHand(new ArgentSquire());
		Minion bloodfenRaptor = new BloodfenRaptor();
		bloodfenRaptor.hasAttacked(true);
		startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, bloodfenRaptor);

		BoardStateFactoryBase factory = new BoardStateFactoryBase(this.deck0, this.deck1);
		HearthTreeNode root = new HearthTreeNode(startingBoard);
		ArrayList<HearthTreeNode> actuals = factory.getNextLayerOfCardBranches(root);
		assertEquals(3, actuals.size());

		BoardModel expectedBoardA = new BoardModel();
		expectedBoardA.placeMinion(PlayerSide.CURRENT_PLAYER, (Minion)bloodfenRaptor.deepCopy());
		expectedBoardA.getCurrentPlayer().setMana(1);
		expectedBoardA.getCurrentPlayer().setMaxMana(2);
		BoardModel expectedBoardB = expectedBoardA.deepCopy();

		Minion expectedMinion = new ArgentSquire();
		expectedMinion.hasBeenUsed(true); // we are checking state immediately after being played
		expectedMinion.hasAttacked(true);

		expectedBoardA.placeMinion(PlayerSide.CURRENT_PLAYER, expectedMinion, 0);
		assertNodeListContainsBoardModel(actuals, expectedBoardA, 0);

		expectedBoardB.placeMinion(PlayerSide.CURRENT_PLAYER, (Minion)expectedMinion.deepCopy(), 1);
		assertNodeListContainsBoardModel(actuals, expectedBoardB, 1);
	}

	@Test
	public void testCardTargetingMinionsMultiple() throws HSException {
		BoardModel startingBoard = new BoardModel();
		PlayerModel firstPlayer = startingBoard.getCurrentPlayer();
		firstPlayer.addMana(4);
		firstPlayer.addMaxMana(4);
		firstPlayer.placeCardHand(new ShadowBolt());
		startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new BloodfenRaptor());
		startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());

		BoardStateFactoryBase factory = new BoardStateFactoryBase(this.deck0, this.deck1);
		HearthTreeNode root = new HearthTreeNode(startingBoard);
		ArrayList<HearthTreeNode> actuals = factory.getNextLayerOfCardBranches(root);
		assertEquals(3, actuals.size());

		BoardModel expectedBoardA = new BoardModel();
		expectedBoardA.getCurrentPlayer().setMana(1);
		expectedBoardA.getCurrentPlayer().setMaxMana(4);
		expectedBoardA.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());
		
		assertNodeListContainsBoardModel(actuals, expectedBoardA);

		BoardModel expectedBoardB = new BoardModel();
		expectedBoardB.getCurrentPlayer().setMana(1);
		expectedBoardB.getCurrentPlayer().setMaxMana(4);
		expectedBoardB.placeMinion(PlayerSide.WAITING_PLAYER, new BloodfenRaptor());

		assertNodeListContainsBoardModel(actuals, expectedBoardB);
	}

	@Test
	public void testCardTargetingHeros() throws HSException {
		BoardModel startingBoard = new BoardModel();
		PlayerModel firstPlayer = startingBoard.getCurrentPlayer();
		firstPlayer.addMana(2);
		firstPlayer.addMaxMana(2);
		firstPlayer.placeCardHand(new HolySmite());

		BoardStateFactoryBase factory = new BoardStateFactoryBase(this.deck0, this.deck1);
		HearthTreeNode root = new HearthTreeNode(startingBoard);
		ArrayList<HearthTreeNode> actuals = factory.getNextLayerOfCardBranches(root);
		assertEquals(3, actuals.size());

		BoardModel expectedBoardA = new BoardModel();
		expectedBoardA.getCurrentPlayer().setMana(1);
		expectedBoardA.getCurrentPlayer().setMaxMana(2);
		BoardModel expectedBoardB = expectedBoardA.deepCopy();
		
		expectedBoardA.getCharacter(PlayerSide.CURRENT_PLAYER, 0).setHealth((byte)28);
		expectedBoardB.getCharacter(PlayerSide.WAITING_PLAYER, 0).setHealth((byte)28);

		assertNodeListContainsBoardModel(actuals, expectedBoardA);
		assertNodeListContainsBoardModel(actuals, expectedBoardB);
	}

	@Test
	public void testHeropowerTargetingHeros() throws HSException {
		BoardModel startingBoard = new BoardModel(new Mage(), new TestHero());
		PlayerModel firstPlayer = startingBoard.getCurrentPlayer();
		firstPlayer.addMana(2);
		firstPlayer.addMaxMana(2);

		BoardStateFactoryBase factory = new BoardStateFactoryBase(this.deck0, this.deck1);
		HearthTreeNode root = new HearthTreeNode(startingBoard);
		ArrayList<HearthTreeNode> actuals = factory.getNextLayerOfHeroAbilityBranches(root);
		assertEquals(3, actuals.size());

		BoardModel expectedBoardA = new BoardModel(new Mage(), new TestHero());
		expectedBoardA.getCurrentPlayer().setMana(0);
		expectedBoardA.getCurrentPlayer().setMaxMana(2);
		expectedBoardA.getCharacter(PlayerSide.CURRENT_PLAYER, 0).hasBeenUsed(true);
		BoardModel expectedBoardB = expectedBoardA.deepCopy();
		
		expectedBoardA.getCharacter(PlayerSide.CURRENT_PLAYER, 0).setHealth((byte)29);
		expectedBoardB.getCharacter(PlayerSide.WAITING_PLAYER, 0).setHealth((byte)29);

		assertNodeListContainsBoardModel(actuals, expectedBoardA);
		assertNodeListContainsBoardModel(actuals, expectedBoardB);
	}

	@Test
	public void testHeropowerTargetingMinionsMultiple() throws HSException {
		BoardModel startingBoard = new BoardModel(new Mage(), new TestHero());
		PlayerModel firstPlayer = startingBoard.getCurrentPlayer();
		firstPlayer.addMana(2);
		firstPlayer.addMaxMana(2);
		startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
		startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());

		BoardStateFactoryBase factory = new BoardStateFactoryBase(this.deck0, this.deck1);
		HearthTreeNode root = new HearthTreeNode(startingBoard);
		ArrayList<HearthTreeNode> actuals = factory.getNextLayerOfHeroAbilityBranches(root);
		assertEquals(5, actuals.size());

		BoardModel expectedBoardA = new BoardModel(new Mage(), new TestHero());
		expectedBoardA.getCurrentPlayer().setMana(0);
		expectedBoardA.getCurrentPlayer().setMaxMana(2);
		expectedBoardA.getCharacter(PlayerSide.CURRENT_PLAYER, 0).hasBeenUsed(true);
		expectedBoardA.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
		expectedBoardA.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());
		BoardModel expectedBoardB = expectedBoardA.deepCopy();

		expectedBoardA.getCharacter(PlayerSide.CURRENT_PLAYER, 1).setHealth((byte)1);
		assertNodeListContainsBoardModel(actuals, expectedBoardA);

		expectedBoardB.getCharacter(PlayerSide.WAITING_PLAYER, 1).setHealth((byte)2);
		assertNodeListContainsBoardModel(actuals, expectedBoardB);
	}

	@Test
	public void testMinionAttackingMinionsMultiple() throws HSException {
		BoardModel startingBoard = new BoardModel();
		startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
		startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());
		startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new Boar());

		BoardStateFactoryBase factory = new BoardStateFactoryBase(this.deck0, this.deck1);
		HearthTreeNode root = new HearthTreeNode(startingBoard);
		ArrayList<HearthTreeNode> actuals = factory.getNextLayerOfAttackBranches(root);
		assertEquals(4, actuals.size());

		// Killed Boar
		BoardModel expectedBoardA = new BoardModel();
		expectedBoardA.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
		expectedBoardA.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());
		expectedBoardA.getCharacter(PlayerSide.CURRENT_PLAYER, 1).hasAttacked(true);
		expectedBoardA.getCharacter(PlayerSide.CURRENT_PLAYER, 1).setHealth((byte)1);

		assertNodeListContainsBoardModel(actuals, expectedBoardA);
		
		// Killed Crocolisk
		BoardModel expectedBoardB = new BoardModel();
		expectedBoardB.placeMinion(PlayerSide.WAITING_PLAYER, new Boar());

		assertNodeListContainsBoardModel(actuals, expectedBoardB);

		// Attacked Hero
		BoardModel expectedBoardC = new BoardModel();
		expectedBoardC.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
		expectedBoardC.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());
		expectedBoardC.placeMinion(PlayerSide.WAITING_PLAYER, new Boar());
		expectedBoardC.getCharacter(PlayerSide.CURRENT_PLAYER, 1).hasAttacked(true);
		expectedBoardC.getCharacter(PlayerSide.WAITING_PLAYER, 0).setHealth((byte)27);

		assertNodeListContainsBoardModel(actuals, expectedBoardC);
	}

	@Test
	public void testHeroAttackingMinionsMultiple() throws HSException {
		BoardModel startingBoard = new BoardModel();
		startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());
		startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new Boar());
		startingBoard.getCharacter(PlayerSide.CURRENT_PLAYER, 0).addAttack((byte)1);

		BoardStateFactoryBase factory = new BoardStateFactoryBase(this.deck0, this.deck1);
		HearthTreeNode root = new HearthTreeNode(startingBoard);
		ArrayList<HearthTreeNode> actuals = factory.getNextLayerOfAttackBranches(root);
		assertEquals(4, actuals.size());

		// Killed Boar
		BoardModel expectedBoardA = new BoardModel();
		expectedBoardA.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());
		expectedBoardA.getCharacter(PlayerSide.CURRENT_PLAYER, 0).addAttack((byte)1);
		expectedBoardA.getCharacter(PlayerSide.CURRENT_PLAYER, 0).hasAttacked(true);
		expectedBoardA.getCharacter(PlayerSide.CURRENT_PLAYER, 0).setHealth((byte)29);

		assertNodeListContainsBoardModel(actuals, expectedBoardA, 2);
		
		// Attacked Crocolisk
		BoardModel expectedBoardB = new BoardModel();
		expectedBoardB.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());
		expectedBoardB.placeMinion(PlayerSide.WAITING_PLAYER, new Boar());
		expectedBoardB.getCharacter(PlayerSide.CURRENT_PLAYER, 0).addAttack((byte)1);
		expectedBoardB.getCharacter(PlayerSide.CURRENT_PLAYER, 0).hasAttacked(true);
		expectedBoardB.getCharacter(PlayerSide.CURRENT_PLAYER, 0).setHealth((byte)28);
		expectedBoardB.getCharacter(PlayerSide.WAITING_PLAYER, 1).setHealth((byte)2);

		assertNodeListContainsBoardModel(actuals, expectedBoardB);

		// Attacked Hero
		BoardModel expectedBoardC = new BoardModel();
		expectedBoardC.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());
		expectedBoardC.placeMinion(PlayerSide.WAITING_PLAYER, new Boar());
		expectedBoardC.getCharacter(PlayerSide.CURRENT_PLAYER, 0).addAttack((byte)1);
		expectedBoardC.getCharacter(PlayerSide.CURRENT_PLAYER, 0).hasAttacked(true);
		expectedBoardC.getCharacter(PlayerSide.WAITING_PLAYER, 0).setHealth((byte)29);

		assertNodeListContainsBoardModel(actuals, expectedBoardC);
	}

	private void assertNodeListContainsBoardModel(ArrayList<HearthTreeNode> nodes, BoardModel expectedBoard) {
		this.assertNodeListContainsBoardModel(nodes, expectedBoard, 0);
	}

	private void assertNodeListContainsBoardModel(ArrayList<HearthTreeNode> nodes, BoardModel expectedBoard,
			int indexHint) {
		boolean success = false;
		for(HearthTreeNode node : nodes) {
			if(node.data_.equals(expectedBoard)) {
				success = true;
				break;
			}
		}

		// TODO kind of an ugly hack to get output from the test. should create better assertEquals variants for board state
		if(!success) {
			assertEquals(expectedBoard.getCurrentPlayer().getMinions(), nodes.get(indexHint).data_.getCurrentPlayer().getMinions());
			assertEquals(expectedBoard.getWaitingPlayer().getMinions(), nodes.get(indexHint).data_.getWaitingPlayer().getMinions());
			assertEquals(expectedBoard.getCurrentPlayer().getHero(), nodes.get(indexHint).data_.getCurrentPlayer().getHero());
			assertEquals(expectedBoard.getWaitingPlayer().getHero(), nodes.get(indexHint).data_.getWaitingPlayer().getHero());
			assertEquals(expectedBoard, nodes.get(indexHint).data_);
		} else {
			assertTrue(success);
		}
	}
}
