package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.GrimscaleOracle;
import com.hearthsim.card.minion.concrete.MurlocRaider;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestGrimscaleOracle {
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

        Minion minion0_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion0_1 = new Minion("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);
        Minion minion1_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion1_1 = new Minion("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_1);

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_1);

        Minion fb = new GrimscaleOracle();
        currentPlayer.placeCardHand(fb);

        currentPlayer.setMana((byte) 7);
    }

    @Test
    public void testBuffsOwnMurloc() throws HSException {
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new MurlocRaider());

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board);

        assertEquals(board, ret);

        assertEquals(currentPlayer.getMinions().get(0).getHealth(), health0);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 1);
        assertEquals(currentPlayer.getMinions().get(2).getHealth(), health1 - 1);
        assertEquals(currentPlayer.getMinions().get(3).getHealth(), 1);

        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), health0);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), health1 - 1);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), attack0);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 1);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), attack0);
        assertEquals(currentPlayer.getMinions().get(3).getTotalAttack(), 3);

        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), attack0);
    }

    @Test
    public void testBuffsEnemyMurloc() throws HSException {
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new MurlocRaider());

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board);

        assertEquals(board, ret);

        assertEquals(currentPlayer.getMinions().get(0).getHealth(), health0);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 1);
        assertEquals(currentPlayer.getMinions().get(2).getHealth(), health1 - 1);

        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), health0);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), health1 - 1);
        assertEquals(waitingPlayer.getMinions().get(2).getHealth(), 1);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), attack0);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 1);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), attack0);

        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalAttack(), 3);
    }

    @Test
    public void testBuffsMurlocPlacedAfter() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board);
        assertEquals(board, ret);

        MurlocRaider murloc = new MurlocRaider();
        currentPlayer.placeCardHand(murloc);
        ret = murloc.useOn(PlayerSide.CURRENT_PLAYER, 1, board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 1);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 3);
    }
}
