package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.GurubashiBerserker;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestGurubashiBerserker {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;

    private static final byte mana = 2;
    private static final byte attack0 = 5;
    private static final byte health0 = 3;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();

        Minion minion1_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);

        Minion fb = new GurubashiBerserker();
        currentPlayer.placeCardHand(fb);

        currentPlayer.setMana((byte) 7);
    }

    @Test
    public void testBuffsAfterAttackingEnemyMinion() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board);
        assertEquals(board, ret);

        Minion berserker = currentPlayer.getMinions().get(0);
        berserker.hasAttacked(false);
        ret = berserker.attack(PlayerSide.WAITING_PLAYER, 1, board, false);
        assertEquals(board, ret);

        assertEquals(berserker.getHealth(), 7 - attack0);
        assertEquals(berserker.getTotalAttack(), 5);
    }

    @Test
    public void testDivineShieldPreventsBuff() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board);
        assertEquals(board, ret);

        Minion berserker = currentPlayer.getMinions().get(0);
        berserker.hasAttacked(false);
        berserker.setDivineShield(true);
        ret = berserker.attack(PlayerSide.WAITING_PLAYER, 1, board, false);
        assertEquals(board, ret);

        assertEquals(berserker.getHealth(), 7);
        assertEquals(berserker.getTotalAttack(), 2);
        assertFalse(berserker.getDivineShield());
    }
}
