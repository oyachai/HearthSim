package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.LeperGnome;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestLeperGnome {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BoulderfistOgre());

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new BoulderfistOgre());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new LeperGnome());

        Card fb = new LeperGnome();
        currentPlayer.placeCardHand(fb);

        currentPlayer.setMana((byte) 8);
        waitingPlayer.setMana((byte) 8);
    }

    @Test
    public void test0() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 0, board);

        assertNull(ret);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 3);
        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getMinions().get(0).getTotalHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalHealth(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalHealth(), 7);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalHealth(), 1);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalAttack(), 3);
    }

    @Test
    public void test2() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 2, board);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 3);
        assertEquals(currentPlayer.getMana(), 7);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getMinions().get(0).getTotalHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalHealth(), 7);
        assertEquals(currentPlayer.getMinions().get(2).getTotalHealth(), 1);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalHealth(), 7);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalHealth(), 1);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 7);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 3);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalAttack(), 3);

        //----------------------------------------------------------

        Minion minion = currentPlayer.getMinions().get(2);
        assertTrue(minion instanceof LeperGnome);

        minion.hasAttacked(false);
        minion.attack(PlayerSide.WAITING_PLAYER, 2, board, false);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 3);
        assertEquals(currentPlayer.getMana(), 7);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 28);
        assertEquals(currentPlayer.getMinions().get(0).getTotalHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalHealth(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalHealth(), 7 - 3);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalHealth(), 1);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalAttack(), 3);

        //----------------------------------------------------------

        minion = currentPlayer.getMinions().get(1);

        minion.hasAttacked(false);
        minion.attack(PlayerSide.WAITING_PLAYER, 3, board, false);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 7);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 28);
        assertEquals(waitingPlayer.getHero().getHealth(), 28);
        assertEquals(currentPlayer.getMinions().get(0).getTotalHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalHealth(), 7 - 3);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalHealth(), 7 - 3);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 7);
    }
}
