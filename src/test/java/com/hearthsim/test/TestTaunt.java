package com.hearthsim.test;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.spell.HolySmite;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionMock;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.BoardScorer;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.factory.DepthBoardStateFactory;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestTaunt {

    private BoardModel board;
    private DummyStateFunc scoreFunc;
    private static final byte mana = 1;
    private static final byte attack0 = 2;
    private static final byte health0 = 3;
    private static final byte health1 = 7;

    private class DummyStateFunc implements BoardScorer {

        @Override
        public double boardScore(BoardModel xval) {
            return 0;
        }

        @Override
        public double cardInHandScore(Card card, BoardModel board) {
            return 0;
        }

        @Override
        public double heroHealthScore_p0(double heroHealth, double heroArmor) {
            return 0;
        }

        @Override
        public double heroHealthScore_p1(double heroHealth, double heroArmor) {
            return 0;
        }

        @Override
        public double minionOnBoardScore(Minion minion, PlayerSide side,
                BoardModel board) {
            return 0;
        }
    }

    @Before
    public void setup() throws HSException {
        board = new BoardModel();
        scoreFunc = new DummyStateFunc();

        Minion minion0_0 = new MinionMock("" + 0, mana, attack0, health0, attack0, health0, health0);

        board.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);

        Minion minion = new MinionMock("" + 0, mana, attack0, health1, attack0, health1, health1);
        minion.setTaunt(true);
        board.placeMinion(PlayerSide.WAITING_PLAYER, minion);
    }

    @Test
    public void testBlocksAttacksAgainstHeros() throws HSException {
        BoardStateFactoryBase factory = new DepthBoardStateFactory(null, null, true);
        HearthTreeNode tree = new HearthTreeNode(board);
        try {
            factory.doMoves(tree, scoreFunc);
        } catch(HSException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        // 2 possibilities:
        // 1. Do nothing
        // 2. Attack the Taunt minion
    }

    @Test
    public void testBlocksAttacksAgainstMinions() throws HSException {
        Minion minion2 = new MinionMock("" + 0, mana, attack0, health1, attack0, health1, health1);
        board.placeMinion(PlayerSide.WAITING_PLAYER, minion2);

        BoardStateFactoryBase factory = new DepthBoardStateFactory(null, null, true);
        HearthTreeNode tree = new HearthTreeNode(board);
        try {
            factory.doMoves(tree, scoreFunc);
        } catch(HSException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        // 2 possibilities:
        // 1. Do nothing
        // 2. Attack the Taunt minion
    }

    @Test
    public void testCanAttackEitherTaunt() throws HSException {
        Minion minion2 = new MinionMock("" + 0, mana, attack0, health1, attack0, health1, health1);
        Minion minion3 = new MinionMock("" + 0, mana, attack0, health1, attack0, health1, health1);
        minion3.setTaunt(true);

        board.placeMinion(PlayerSide.WAITING_PLAYER, minion2);
        board.placeMinion(PlayerSide.WAITING_PLAYER, minion3);

        BoardStateFactoryBase factory = new DepthBoardStateFactory(null, null, true);
        HearthTreeNode tree = new HearthTreeNode(board);
        try {
            factory.doMoves(tree, scoreFunc);
        } catch(HSException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        // 3 possibilities:
        // 1. Do nothing
        // 2. Attack the Taunt minion1
        // 3. Attack the Taunt minion2
    }

    @Test
    public void testCannotCastSpellWithoutMana() throws HSException {
        HolySmite holySmite = new HolySmite();

        board.removeMinion(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1);
        board.getCurrentPlayer().placeCardHand(holySmite);

        BoardStateFactoryBase factory = new DepthBoardStateFactory(null, null, 2000000000, true);
        HearthTreeNode tree = new HearthTreeNode(board);
        try {
            factory.doMoves(tree, scoreFunc);
        } catch(HSException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        // 1 possibility:
        // 1. Do nothing (not enough mana!)
    }

    @Test
    public void testDoesNotBlockSpells() throws HSException {
        HolySmite holySmite = new HolySmite();

        board.removeMinion(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1);
        board.getCurrentPlayer().placeCardHand(holySmite);
        board.getCurrentPlayer().setMana((byte)1);

        BoardStateFactoryBase factory = new DepthBoardStateFactory(null, null, true);
        HearthTreeNode tree = new HearthTreeNode(board);
        try {
            factory.doMoves(tree, scoreFunc);
        } catch(HSException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        // 4 possibilities:
        // 1. Do nothing
        // 2. Use HS on the Taunt minion1
        // 3. Use HS on the enemy hero
        // 4. Use HS on the my own hero
    }
}
