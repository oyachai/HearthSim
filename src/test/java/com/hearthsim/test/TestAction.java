package com.hearthsim.test;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionMock;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.util.HearthAction.Verb;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.factory.DepthBoardStateFactory;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestAction {

    private BoardModel board;
    private static final byte mana = 1;
    private static final byte attack0 = 2;
    private static final byte attack1 = 10;
    private static final byte health0 = 3;
    private static final byte health1 = 7;
    private static final byte health2 = 10;

    @Before
    public void setup() throws HSException {
        board = new BoardModel();

        Minion minion0_0 = new MinionMock("alpha", mana, attack0, health0, attack0, health0, health0);

        board.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);
    }

    @Test
    public void testAiShouldAttackMinion() throws HSException {
        Minion minion = new MinionMock("beta", mana, attack0, health1, attack0, health1, health1);
        board.placeMinion(PlayerSide.WAITING_PLAYER, minion);

        BoardStateFactoryBase factory = new DepthBoardStateFactory(null, null, 2000000000, true);
        HearthTreeNode tree = new HearthTreeNode(board);
        try {
            tree = factory.doMoves(tree, BruteForceSearchAI.buildStandardAI2().scorer);
        } catch(HSException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertEquals(tree.getChildren().get(0).getAction().verb_, Verb.ATTACK);
        assertEquals(tree.getChildren().get(0).getAction().targetCharacterIndex, CharacterIndex.MINION_1); // should hit the enemy minion
    }

    @Test
    public void testAiShouldAttackOpponent() throws HSException {
        Minion minion = new MinionMock("beta", mana, attack1, health2, attack1, health2, health2);
        board.placeMinion(PlayerSide.WAITING_PLAYER, minion);

        BoardStateFactoryBase factory = new DepthBoardStateFactory(null, null, 2000000000, true);
        HearthTreeNode tree = new HearthTreeNode(board);
        try {
            tree = factory.doMoves(tree, BruteForceSearchAI.buildStandardAI2().scorer);
        } catch(HSException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertEquals(tree.getChildren().get(0).getAction().verb_, Verb.ATTACK);
        assertEquals(tree.getChildren().get(0).getAction().targetCharacterIndex, CharacterIndex.HERO); // should hit the enemy hero
    }
}
