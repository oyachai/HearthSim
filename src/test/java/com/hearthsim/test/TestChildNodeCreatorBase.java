package com.hearthsim.test;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.Deck;
import com.hearthsim.card.basic.minion.*;
import com.hearthsim.card.basic.spell.AnimalCompanion;
import com.hearthsim.card.basic.spell.HolySmite;
import com.hearthsim.card.basic.spell.ShadowBolt;
import com.hearthsim.card.basic.spell.TheCoin;
import com.hearthsim.card.classic.minion.common.ArgentSquire;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.heroes.Mage;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.factory.ChildNodeCreatorBase;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestChildNodeCreatorBase {
    protected final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private Deck deck0;
    private Deck deck1;

    @Before
    public void setup() {
        Card cards[] = new Card[10];
        for (int index = 0; index < 10; ++index) {
            cards[index] = new TheCoin();
        }

        deck0 = new Deck(cards);
        deck1 = deck0.deepCopy();
    }

    @Test
    public void testMinionPlacementSingle() throws HSException {
        BoardModel startingBoard = new BoardModel();
        PlayerModel firstPlayer = startingBoard.getCurrentPlayer();
        firstPlayer.addMana((byte)2);
        firstPlayer.addMaxMana((byte)2);
        firstPlayer.placeCardHand(new BloodfenRaptor());

        BoardModel expectedBoard = new BoardModel();
        Minion expectedMinion = new BloodfenRaptor();
        expectedMinion.hasBeenUsed(true); // we are checking state immediately after being played
        expectedMinion.hasAttacked(true);
        expectedBoard.placeMinion(PlayerSide.CURRENT_PLAYER, expectedMinion);
        expectedBoard.getCurrentPlayer().setMana((byte)0);
        expectedBoard.getCurrentPlayer().setMaxMana((byte)2);
        expectedBoard.getCurrentPlayer().setNumCardsUsed((byte) 1);

        ChildNodeCreatorBase factory = new ChildNodeCreatorBase(this.deck0, this.deck1);
        HearthTreeNode root = new HearthTreeNode(startingBoard);

        ArrayList<HearthTreeNode> actuals = factory.createPlayCardChildren(root);
        assertEquals(2, actuals.size());
        assertNodeListContainsBoardModel(actuals, expectedBoard);
        assertNodeDoesNotContainDuplicates(actuals);
        assertNodeListActionsAreRepeatable(startingBoard, actuals);
    }

    @Test
    public void testMinionPlacementMultiple() throws HSException {
        BoardModel startingBoard = new BoardModel();
        startingBoard.getCurrentPlayer().addMana((byte)2);
        startingBoard.getCurrentPlayer().addMaxMana((byte)2);
        startingBoard.getCurrentPlayer().placeCardHand(new BloodfenRaptor());
        startingBoard.getCurrentPlayer().placeCardHand(new ArgentSquire());

        ChildNodeCreatorBase factory = new ChildNodeCreatorBase(this.deck0, this.deck1);
        HearthTreeNode root = new HearthTreeNode(startingBoard);
        ArrayList<HearthTreeNode> actuals = factory.createPlayCardChildren(root);
        assertEquals(3, actuals.size());
        assertNodeDoesNotContainDuplicates(actuals);
        assertNodeListActionsAreRepeatable(startingBoard, actuals);

        BoardModel expectedBoard = new BoardModel();
        Minion expectedMinion = new BloodfenRaptor();
        expectedMinion.hasBeenUsed(true); // we are checking state immediately after being played
        expectedMinion.hasAttacked(true);
        expectedBoard.placeMinion(PlayerSide.CURRENT_PLAYER, expectedMinion);

        expectedBoard.getCurrentPlayer().placeCardHand(new ArgentSquire());
        expectedBoard.getCurrentPlayer().setMana((byte) 0);
        expectedBoard.getCurrentPlayer().setMaxMana((byte) 2);
        expectedBoard.getCurrentPlayer().addNumCardsUsed((byte)1);
        assertNodeListContainsBoardModel(actuals, expectedBoard, 1);

        expectedBoard = new BoardModel();
        expectedMinion = new ArgentSquire();
        expectedMinion.hasBeenUsed(true); // we are checking state immediately after being played
        expectedMinion.hasAttacked(true);
        expectedBoard.placeMinion(PlayerSide.CURRENT_PLAYER, expectedMinion);

        expectedBoard.getCurrentPlayer().placeCardHand(new BloodfenRaptor());
        expectedBoard.getCurrentPlayer().setMana((byte) 1);
        expectedBoard.getCurrentPlayer().setMaxMana((byte) 2);
        expectedBoard.getCurrentPlayer().addNumCardsUsed((byte) 1);
        assertNodeListContainsBoardModel(actuals, expectedBoard, 0);
    }

    @Test
    public void testMinionPlacementNotEnoughMana() throws HSException {
        BoardModel startingBoard = new BoardModel();
        PlayerModel firstPlayer = startingBoard.getCurrentPlayer();
        firstPlayer.addMana((byte)1);
        firstPlayer.addMaxMana((byte)1);
        firstPlayer.placeCardHand(new BloodfenRaptor());

        ChildNodeCreatorBase factory = new ChildNodeCreatorBase(this.deck0, this.deck1);
        HearthTreeNode root = new HearthTreeNode(startingBoard);

        ArrayList<HearthTreeNode> actuals = factory.createPlayCardChildren(root);
        assertEquals(0, actuals.size());
    }

    @Test
    public void testMinionPlacementPositioning() throws HSException {
        BoardModel startingBoard = new BoardModel();
        startingBoard.getCurrentPlayer().addMana((byte)2);
        startingBoard.getCurrentPlayer().addMaxMana((byte)2);
        startingBoard.getCurrentPlayer().placeCardHand(new ArgentSquire());
        Minion bloodfenRaptor = new BloodfenRaptor();
        bloodfenRaptor.hasAttacked(true);
        startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, bloodfenRaptor);

        ChildNodeCreatorBase factory = new ChildNodeCreatorBase(this.deck0, this.deck1);
        HearthTreeNode root = new HearthTreeNode(startingBoard);
        ArrayList<HearthTreeNode> actuals = factory.createPlayCardChildren(root);
        assertEquals(3, actuals.size());
        assertNodeDoesNotContainDuplicates(actuals);
        assertNodeListActionsAreRepeatable(startingBoard, actuals);

        BoardModel expectedBoardA = new BoardModel();
        expectedBoardA.placeMinion(PlayerSide.CURRENT_PLAYER, (Minion)bloodfenRaptor.deepCopy());
        expectedBoardA.getCurrentPlayer().setMana((byte)1);
        expectedBoardA.getCurrentPlayer().setMaxMana((byte)2);
        expectedBoardA.getCurrentPlayer().setNumCardsUsed((byte)1);
        BoardModel expectedBoardB = expectedBoardA.deepCopy();

        Minion expectedMinion = new ArgentSquire();
        expectedMinion.hasBeenUsed(true); // we are checking state immediately after being played
        expectedMinion.hasAttacked(true);

        expectedBoardA.placeMinion(PlayerSide.CURRENT_PLAYER, expectedMinion, CharacterIndex.HERO);
        assertNodeListContainsBoardModel(actuals, expectedBoardA, 0);

        expectedBoardB.placeMinion(PlayerSide.CURRENT_PLAYER, (Minion)expectedMinion.deepCopy(), CharacterIndex.MINION_1);
        assertNodeListContainsBoardModel(actuals, expectedBoardB, 1);
    }

    @Test
    public void testCardTargetingMinionsMultiple() throws HSException {
        BoardModel startingBoard = new BoardModel();
        PlayerModel firstPlayer = startingBoard.getCurrentPlayer();
        firstPlayer.addMana((byte)4);
        firstPlayer.addMaxMana((byte)4);
        firstPlayer.placeCardHand(new ShadowBolt());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());

        ChildNodeCreatorBase factory = new ChildNodeCreatorBase(this.deck0, this.deck1);
        HearthTreeNode root = new HearthTreeNode(startingBoard);
        ArrayList<HearthTreeNode> actuals = factory.createPlayCardChildren(root);
        assertEquals(3, actuals.size());
        assertNodeDoesNotContainDuplicates(actuals);
        assertNodeListActionsAreRepeatable(startingBoard, actuals);

        BoardModel expectedBoardA = new BoardModel();
        expectedBoardA.getCurrentPlayer().setMana((byte)1);
        expectedBoardA.getCurrentPlayer().setMaxMana((byte)4);
        expectedBoardA.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());
        expectedBoardA.getCurrentPlayer().setNumCardsUsed((byte)1);

        assertNodeListContainsBoardModel(actuals, expectedBoardA);

        BoardModel expectedBoardB = new BoardModel();
        expectedBoardB.getCurrentPlayer().setMana((byte)1);
        expectedBoardB.getCurrentPlayer().setMaxMana((byte) 4);
        expectedBoardB.getCurrentPlayer().addNumCardsUsed((byte)1);
        expectedBoardB.placeMinion(PlayerSide.WAITING_PLAYER, new BloodfenRaptor());

        assertNodeListContainsBoardModel(actuals, expectedBoardB);
    }

    @Test
    public void testCardTargetingHeros() throws HSException {
        BoardModel startingBoard = new BoardModel();
        PlayerModel firstPlayer = startingBoard.getCurrentPlayer();
        firstPlayer.addMana((byte)2);
        firstPlayer.addMaxMana((byte)2);
        firstPlayer.placeCardHand(new HolySmite());

        ChildNodeCreatorBase factory = new ChildNodeCreatorBase(this.deck0, this.deck1);
        HearthTreeNode root = new HearthTreeNode(startingBoard);
        ArrayList<HearthTreeNode> actuals = factory.createPlayCardChildren(root);
        assertEquals(3, actuals.size());
        assertNodeDoesNotContainDuplicates(actuals);
        assertNodeListActionsAreRepeatable(startingBoard, actuals);

        BoardModel expectedBoardA = new BoardModel();
        firstPlayer = expectedBoardA.getCurrentPlayer();
        firstPlayer.setMana((byte) 1);
        firstPlayer.setMaxMana((byte) 2);
        firstPlayer.setNumCardsUsed((byte)1);

        BoardModel expectedBoardB = expectedBoardA.deepCopy();

        firstPlayer.getCharacter(CharacterIndex.HERO).setHealth((byte)28);
        expectedBoardB.modelForSide(PlayerSide.WAITING_PLAYER).getCharacter(CharacterIndex.HERO).setHealth((byte)28);

        assertNodeListContainsBoardModel(actuals, expectedBoardA);
        assertNodeListContainsBoardModel(actuals, expectedBoardB);
    }

    @Test
    public void testHeropowerTargetingHeros() throws HSException {
        BoardModel startingBoard = new BoardModel(new Mage(), new TestHero());
        PlayerModel firstPlayer = startingBoard.getCurrentPlayer();
        firstPlayer.addMana((byte)2);
        firstPlayer.addMaxMana((byte)2);

        ChildNodeCreatorBase factory = new ChildNodeCreatorBase(this.deck0, this.deck1);
        HearthTreeNode root = new HearthTreeNode(startingBoard);
        ArrayList<HearthTreeNode> actuals = factory.createHeroAbilityChildren(root);
        assertEquals(2, actuals.size());
        assertNodeDoesNotContainDuplicates(actuals);
        assertNodeListActionsAreRepeatable(startingBoard, actuals);

        BoardModel expectedBoardA = new BoardModel(new Mage(), new TestHero());
        expectedBoardA.getCurrentPlayer().setMana((byte)0);
        expectedBoardA.getCurrentPlayer().setMaxMana((byte)2);
        expectedBoardA.modelForSide(PlayerSide.CURRENT_PLAYER).getCharacter(CharacterIndex.HERO).hasBeenUsed(true);
        BoardModel expectedBoardB = expectedBoardA.deepCopy();

        expectedBoardA.modelForSide(PlayerSide.CURRENT_PLAYER).getCharacter(CharacterIndex.HERO).setHealth((byte)29);
        expectedBoardB.modelForSide(PlayerSide.WAITING_PLAYER).getCharacter(CharacterIndex.HERO).setHealth((byte)29);

//        assertNodeListContainsBoardModel(actuals, expectedBoardA);
        assertNodeListContainsBoardModel(actuals, expectedBoardB);
    }

    @Test
    public void testHeropowerTargetingMinionsMultiple() throws HSException {
        BoardModel startingBoard = new BoardModel(new Mage(), new TestHero());
        PlayerModel firstPlayer = startingBoard.getCurrentPlayer();
        firstPlayer.addMana((byte)2);
        firstPlayer.addMaxMana((byte)2);
        startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());

        ChildNodeCreatorBase factory = new ChildNodeCreatorBase(this.deck0, this.deck1);
        HearthTreeNode root = new HearthTreeNode(startingBoard);
        ArrayList<HearthTreeNode> actuals = factory.createHeroAbilityChildren(root);
        assertEquals(4, actuals.size());
        assertNodeDoesNotContainDuplicates(actuals);
        assertNodeListActionsAreRepeatable(startingBoard, actuals);

        BoardModel expectedBoardA = new BoardModel(new Mage(), new TestHero());
        expectedBoardA.getCurrentPlayer().setMana((byte)0);
        expectedBoardA.getCurrentPlayer().setMaxMana((byte)2);
        expectedBoardA.modelForSide(PlayerSide.CURRENT_PLAYER).getCharacter(CharacterIndex.HERO).hasBeenUsed(true);
        expectedBoardA.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
        expectedBoardA.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());
        BoardModel expectedBoardB = expectedBoardA.deepCopy();

        expectedBoardA.modelForSide(PlayerSide.CURRENT_PLAYER).getCharacter(CharacterIndex.MINION_1).setHealth((byte)1);
        assertNodeListContainsBoardModel(actuals, expectedBoardA);

        expectedBoardB.modelForSide(PlayerSide.WAITING_PLAYER).getCharacter(CharacterIndex.MINION_1).setHealth((byte)2);
        assertNodeListContainsBoardModel(actuals, expectedBoardB);
    }

    @Test
    public void testMinionAttackingMinionsMultiple() throws HSException {
        BoardModel startingBoard = new BoardModel();
        startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new StonetuskBoar());

        ChildNodeCreatorBase factory = new ChildNodeCreatorBase(this.deck0, this.deck1);
        HearthTreeNode root = new HearthTreeNode(startingBoard);
        ArrayList<HearthTreeNode> actuals = factory.createAttackChildren(root);
        assertEquals(4, actuals.size());
        assertNodeDoesNotContainDuplicates(actuals);
        assertNodeListActionsAreRepeatable(startingBoard, actuals);

        // Killed Boar
        BoardModel expectedBoardA = new BoardModel();
        expectedBoardA.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
        expectedBoardA.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());
        expectedBoardA.modelForSide(PlayerSide.CURRENT_PLAYER).getCharacter(CharacterIndex.MINION_1).hasAttacked(true);
        expectedBoardA.modelForSide(PlayerSide.CURRENT_PLAYER).getCharacter(CharacterIndex.MINION_1).setHealth((byte)1);

        assertNodeListContainsBoardModel(actuals, expectedBoardA);

        // Killed Crocolisk
        BoardModel expectedBoardB = new BoardModel();
        expectedBoardB.placeMinion(PlayerSide.WAITING_PLAYER, new StonetuskBoar());

        assertNodeListContainsBoardModel(actuals, expectedBoardB);

        // Attacked Hero
        BoardModel expectedBoardC = new BoardModel();
        expectedBoardC.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
        expectedBoardC.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());
        expectedBoardC.placeMinion(PlayerSide.WAITING_PLAYER, new StonetuskBoar());
        expectedBoardC.modelForSide(PlayerSide.CURRENT_PLAYER).getCharacter(CharacterIndex.MINION_1).hasAttacked(true);
        expectedBoardC.modelForSide(PlayerSide.WAITING_PLAYER).getCharacter(CharacterIndex.HERO).setHealth((byte)27);

        assertNodeListContainsBoardModel(actuals, expectedBoardC);
    }

    @Test
    public void testHeroAttackingMinionsMultiple() throws HSException {
        BoardModel startingBoard = new BoardModel();
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new StonetuskBoar());
        startingBoard.modelForSide(PlayerSide.CURRENT_PLAYER).getCharacter(CharacterIndex.HERO).addAttack((byte)1);

        ChildNodeCreatorBase factory = new ChildNodeCreatorBase(this.deck0, this.deck1);
        HearthTreeNode root = new HearthTreeNode(startingBoard);
        ArrayList<HearthTreeNode> actuals = factory.createAttackChildren(root);
        assertEquals(4, actuals.size());
        assertNodeDoesNotContainDuplicates(actuals);
        assertNodeListActionsAreRepeatable(startingBoard, actuals);

        // Killed Boar
        BoardModel expectedBoardA = new BoardModel();
        expectedBoardA.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());
        expectedBoardA.modelForSide(PlayerSide.CURRENT_PLAYER).getCharacter(CharacterIndex.HERO).addAttack((byte)1);
        expectedBoardA.modelForSide(PlayerSide.CURRENT_PLAYER).getCharacter(CharacterIndex.HERO).hasAttacked(true);
        expectedBoardA.modelForSide(PlayerSide.CURRENT_PLAYER).getCharacter(CharacterIndex.HERO).setHealth((byte)29);

        assertNodeListContainsBoardModel(actuals, expectedBoardA, 2);

        // Attacked Crocolisk
        BoardModel expectedBoardB = new BoardModel();
        expectedBoardB.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());
        expectedBoardB.placeMinion(PlayerSide.WAITING_PLAYER, new StonetuskBoar());
        expectedBoardB.modelForSide(PlayerSide.CURRENT_PLAYER).getCharacter(CharacterIndex.HERO).addAttack((byte)1);
        expectedBoardB.modelForSide(PlayerSide.CURRENT_PLAYER).getCharacter(CharacterIndex.HERO).hasAttacked(true);
        expectedBoardB.modelForSide(PlayerSide.CURRENT_PLAYER).getCharacter(CharacterIndex.HERO).setHealth((byte)28);
        expectedBoardB.modelForSide(PlayerSide.WAITING_PLAYER).getCharacter(CharacterIndex.MINION_1).setHealth((byte)2);

        assertNodeListContainsBoardModel(actuals, expectedBoardB);

        // Attacked Hero
        BoardModel expectedBoardC = new BoardModel();
        expectedBoardC.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());
        expectedBoardC.placeMinion(PlayerSide.WAITING_PLAYER, new StonetuskBoar());
        expectedBoardC.modelForSide(PlayerSide.CURRENT_PLAYER).getCharacter(CharacterIndex.HERO).addAttack((byte)1);
        expectedBoardC.modelForSide(PlayerSide.CURRENT_PLAYER).getCharacter(CharacterIndex.HERO).hasAttacked(true);
        expectedBoardC.modelForSide(PlayerSide.WAITING_PLAYER).getCharacter(CharacterIndex.HERO).setHealth((byte)29);

        assertNodeListContainsBoardModel(actuals, expectedBoardC);
    }

    @Test
    public void testRngCard() throws HSException {
        BoardModel startingBoard = new BoardModel();
        startingBoard.getCurrentPlayer().addMana((byte)3);
        startingBoard.getCurrentPlayer().addMaxMana((byte)3);
        startingBoard.getCurrentPlayer().placeCardHand(new AnimalCompanion());
        startingBoard.getCurrentPlayer().placeCardHand(new ArgentSquire());

        ChildNodeCreatorBase factory = new ChildNodeCreatorBase(this.deck0, this.deck1);
        HearthTreeNode root = new HearthTreeNode(startingBoard);
        ArrayList<HearthTreeNode> actuals = factory.createPlayCardChildren(root);
        assertEquals(3, actuals.size());
        assertNodeDoesNotContainDuplicates(actuals);
        assertNodeListActionsAreRepeatable(startingBoard, actuals);

        BoardModel expectedBoard = new BoardModel();
        Minion expectedMinion = new ArgentSquire();
        expectedMinion.hasBeenUsed(true); // we are checking state immediately after being played
        expectedMinion.hasAttacked(true);
        expectedBoard.placeMinion(PlayerSide.CURRENT_PLAYER, expectedMinion);

        expectedBoard.getCurrentPlayer().placeCardHand(new AnimalCompanion());
        expectedBoard.getCurrentPlayer().setMana((byte)2);
        expectedBoard.getCurrentPlayer().setMaxMana((byte)3);
        expectedBoard.getCurrentPlayer().setNumCardsUsed((byte)1);
        assertNodeListContainsBoardModel(actuals, expectedBoard, 0);

        // TODO using get(0) here is fragile...
        assertEquals(3, actuals.get(0).numChildren());
        List<HearthTreeNode> rngActuals = actuals.get(0).getChildren();

        expectedBoard = new BoardModel();
        expectedMinion = new Huffer();
        expectedMinion.hasBeenUsed(true); // we are checking state immediately after being played
        expectedBoard.placeMinion(PlayerSide.CURRENT_PLAYER, expectedMinion);

        expectedBoard.getCurrentPlayer().placeCardHand(new ArgentSquire());
        expectedBoard.getCurrentPlayer().setMana((byte)0);
        expectedBoard.getCurrentPlayer().setMaxMana((byte)3);
        expectedBoard.getCurrentPlayer().setNumCardsUsed((byte)1);
        assertNodeListContainsBoardModel(rngActuals, expectedBoard, 0);

        expectedBoard = new BoardModel();
        expectedMinion = new Misha();
        expectedMinion.hasBeenUsed(true); // we are checking state immediately after being played
        expectedMinion.hasAttacked(true);
        expectedBoard.placeMinion(PlayerSide.CURRENT_PLAYER, expectedMinion);

        expectedBoard.getCurrentPlayer().placeCardHand(new ArgentSquire());
        expectedBoard.getCurrentPlayer().setMana((byte) 0);
        expectedBoard.getCurrentPlayer().setMaxMana((byte) 3);
        expectedBoard.getCurrentPlayer().setNumCardsUsed((byte) 1);
        assertNodeListContainsBoardModel(rngActuals, expectedBoard, 1);

        expectedBoard = new BoardModel();
        expectedMinion = new Leokk();
        expectedMinion.hasBeenUsed(true); // we are checking state immediately after being played
        expectedMinion.hasAttacked(true);
        expectedBoard.placeMinion(PlayerSide.CURRENT_PLAYER, expectedMinion);

        expectedBoard.getCurrentPlayer().placeCardHand(new ArgentSquire());
        expectedBoard.getCurrentPlayer().setMana((byte)0);
        expectedBoard.getCurrentPlayer().setMaxMana((byte)3);
        expectedBoard.getCurrentPlayer().setNumCardsUsed((byte)1);
        assertNodeListContainsBoardModel(rngActuals, expectedBoard, 1);
    }

    private void assertNodeListActionsAreRepeatable(BoardModel model, List<HearthTreeNode> children) throws HSException {
        for (HearthTreeNode child : children) {
            HearthTreeNode origin = new HearthTreeNode(model.deepCopy());
            assertNotNull(child.getAction());
            HearthTreeNode reproduced = child.getAction().perform(origin);
            assertNotNull(reproduced);
            assertEquals(child.data_, reproduced.data_);
        }
    }

    private void assertNodeListContainsBoardModel(List<HearthTreeNode> nodes, BoardModel expectedBoard) {
        this.assertNodeListContainsBoardModel(nodes, expectedBoard, 0);
    }

    private void assertNodeListContainsBoardModel(List<HearthTreeNode> nodes, BoardModel expectedBoard, int indexHint) {
        boolean success = false;
        for (HearthTreeNode node : nodes) {
            if (node.data_.equals(expectedBoard)) {
                success = true;
                break;
            }
        }

        // TODO kind of an ugly hack to get output from the test. should create better assertEquals variants for board state
        if (!success) {
            assertEquals(expectedBoard.getCurrentPlayer().getMinions(), nodes.get(indexHint).data_.getCurrentPlayer()
                    .getMinions());
            assertEquals(expectedBoard.getWaitingPlayer().getMinions(), nodes.get(indexHint).data_.getWaitingPlayer()
                    .getMinions());
            assertEquals(expectedBoard.getCurrentPlayer().getHero(), nodes.get(indexHint).data_.getCurrentPlayer()
                    .getHero());
            assertEquals(expectedBoard.getWaitingPlayer().getHero(), nodes.get(indexHint).data_.getWaitingPlayer()
                    .getHero());
            assertEquals(expectedBoard.getCurrentPlayer(), nodes.get(indexHint).data_.getCurrentPlayer());
            assertEquals(expectedBoard.getWaitingPlayer(), nodes.get(indexHint).data_.getWaitingPlayer());
            assertEquals(expectedBoard, nodes.get(indexHint).data_);
        } else {
            assertTrue(success);
        }
    }

    private void assertNodeDoesNotContainDuplicates(ArrayList<HearthTreeNode> nodes) {
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.size(); j++) {
                if (i != j) {
                    assertNotEquals(nodes.get(i).data_, nodes.get(j).data_);
                }
            }
        }
    }
}
