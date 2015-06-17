package com.hearthsim.test.spell;

import com.hearthsim.Game;
import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.spell.Corruption;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionMock;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestCorruption {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    private static final byte mana = 2;
    private static final byte attack0 = 5;
    private static final byte health0 = 3;
    private static final byte health1 = 7;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        Minion minion0_0 = new MinionMock("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion0_1 = new MinionMock("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);
        Minion minion1_0 = new MinionMock("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion1_1 = new MinionMock("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_1);

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_1);

        Corruption fb = new Corruption();
        currentPlayer.placeCardHand(fb);

        currentPlayer.setMana((byte) 4);
        waitingPlayer.setMana((byte) 4);

        currentPlayer.setMaxMana((byte) 4);
        waitingPlayer.setMaxMana((byte) 4);
    }

    @Test
    public void testSetsDestroyOnTurnStart() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_1, board);

        assertEquals(board, ret);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 3);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertTrue(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getDestroyOnTurnStart());
    }

    @Test
    public void testMinionIsDestroyedNextTurnStart() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_1, board);
        assertEquals(board, ret);

        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);

        BoardModel nextTurn = Game.beginTurn(board.data_);
        assertNotNull(nextTurn);
        assertEquals(waitingPlayer.getNumMinions(), 1);
    }
}
