package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.minion.concrete.ScarletCrusader;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestScarletCrusader {

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

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new ScarletCrusader());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new BoulderfistOgre());

        Card fb = new ScarletCrusader();
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
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 1);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(2).getHealth(), 7);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalAttack(), 7);

        assertTrue(waitingPlayer.getMinions().get(0).getDivineShield());
    }

    @Test
    public void test2() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 2, board);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 3);
        assertEquals(currentPlayer.getMana(), 5);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 7);
        assertEquals(currentPlayer.getMinions().get(2).getHealth(), 1);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 1);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(2).getHealth(), 7);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 7);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalAttack(), 7);

        assertTrue(waitingPlayer.getMinions().get(0).getDivineShield());
        assertTrue(currentPlayer.getMinions().get(2).getDivineShield());

        //------------------------------------------------------------
        //Attacking with divine shield vs Hero, divine shield should
        // stay on
        //------------------------------------------------------------
        Minion m0 = currentPlayer.getMinions().get(2);
        m0.hasAttacked(false);
        m0.attack(PlayerSide.WAITING_PLAYER, 0, board, false);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 3);
        assertEquals(currentPlayer.getMana(), 5);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 26);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 7);
        assertEquals(currentPlayer.getMinions().get(2).getHealth(), 1);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 1);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(2).getHealth(), 7);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 7);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalAttack(), 7);

        assertTrue(waitingPlayer.getMinions().get(0).getDivineShield());
        assertTrue(currentPlayer.getMinions().get(2).getDivineShield());

        //------------------------------------------------------------
        //Attacking with divine shield
        //------------------------------------------------------------
        Minion m1 = currentPlayer.getMinions().get(2);
        m1.hasAttacked(false);
        m1.attack(PlayerSide.WAITING_PLAYER, 3, board, false);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 3);
        assertEquals(currentPlayer.getMana(), 5);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 26);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 7);
        assertEquals(currentPlayer.getMinions().get(2).getHealth(), 1);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 1);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(2).getHealth(), 3);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 7);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalAttack(), 7);

        assertTrue(waitingPlayer.getMinions().get(0).getDivineShield());
        assertFalse(currentPlayer.getMinions().get(2).getDivineShield());

        //------------------------------------------------------------
        //Being attacked with a divine shield
        //------------------------------------------------------------
        Minion m2 = currentPlayer.getMinions().get(1);
        m2.hasAttacked(false);
        m2.attack(PlayerSide.WAITING_PLAYER, 1, board, false);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 3);
        assertEquals(currentPlayer.getMana(), 5);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 26);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 3);
        assertEquals(currentPlayer.getMinions().get(2).getHealth(), 1);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 1);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(2).getHealth(), 3);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 7);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalAttack(), 7);

        assertFalse(waitingPlayer.getMinions().get(0).getDivineShield());
        assertFalse(currentPlayer.getMinions().get(2).getDivineShield());

        //------------------------------------------------------------
        //Being attacked with a divine shield that wore off
        //------------------------------------------------------------
        Minion m3 = currentPlayer.getMinions().get(2);
        m3.hasAttacked(false);
        m3.attack(PlayerSide.WAITING_PLAYER, 3, board, false);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 5);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 26);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 3);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 1);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 2);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 2);

        assertFalse(waitingPlayer.getMinions().get(0).getDivineShield());
    }
}
