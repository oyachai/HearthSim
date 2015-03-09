package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.*;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestIronbeakOwl {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new HarvestGolem());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new StormwindChampion());

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new LootHoarder());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new Abomination());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new BoulderfistOgre());

        currentPlayer.setMana((byte) 10);
        waitingPlayer.setMana((byte) 10);

        Minion fb = new IronbeakOwl();
        currentPlayer.placeCardHand(fb);
    }

    @Test
    public void test0() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 0, board, null, null);

        assertNull(ret);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 4);
        assertEquals(currentPlayer.getMana(), 10);
        assertEquals(waitingPlayer.getMana(), 10);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);

        assertEquals(currentPlayer.getMinions().get(0).getTotalHealth(), 4);
        assertEquals(currentPlayer.getMinions().get(1).getTotalHealth(), 3);
        assertEquals(currentPlayer.getMinions().get(2).getTotalHealth(), 6);

        assertEquals(waitingPlayer.getMinions().get(0).getTotalHealth(), 1);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalHealth(), 4);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(3).getTotalHealth(), 7);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 4);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 3);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 7);

        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 3);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(3).getTotalAttack(), 7);
    }

    @Test
    public void test1() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 3, board, null, null);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 4);
        assertEquals(waitingPlayer.getNumMinions(), 4);
        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(waitingPlayer.getMana(), 10);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);

        assertEquals(currentPlayer.getMinions().get(0).getTotalHealth(), 4);
        assertEquals(currentPlayer.getMinions().get(1).getTotalHealth(), 3);
        assertEquals(currentPlayer.getMinions().get(2).getTotalHealth(), 6);
        assertEquals(currentPlayer.getMinions().get(3).getTotalHealth(), 2);

        assertEquals(waitingPlayer.getMinions().get(0).getTotalHealth(), 1);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalHealth(), 4);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(3).getTotalHealth(), 7);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 4);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 3);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 7);
        assertEquals(currentPlayer.getMinions().get(3).getTotalAttack(), 4);

        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 3);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(3).getTotalAttack(), 7);

        assertEquals(ret.numChildren(), 7);

        //------------------------------------------------------------------
        //------------------------------------------------------------------

        HearthTreeNode ret0 = ret.getChildren().get(0);
        assertEquals(ret0.data_.getCurrentPlayer().getHand().size(), 0);
        assertEquals(ret0.data_.getCurrentPlayer().getNumMinions(), 4);
        assertEquals(ret0.data_.getWaitingPlayer().getNumMinions(), 4);
        assertEquals(ret0.data_.getCurrentPlayer().getMana(), 8);
        assertEquals(ret0.data_.getWaitingPlayer().getMana(), 10);
        assertEquals(ret0.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(ret0.data_.getWaitingPlayer().getHero().getHealth(), 30);

        assertEquals(ret0.data_.getCurrentPlayer().getMinions().get(0).getTotalHealth(), 4);
        assertEquals(ret0.data_.getCurrentPlayer().getMinions().get(1).getTotalHealth(), 3);
        assertEquals(ret0.data_.getCurrentPlayer().getMinions().get(2).getTotalHealth(), 6);
        assertEquals(ret0.data_.getCurrentPlayer().getMinions().get(3).getTotalHealth(), 2);

        assertEquals(ret0.data_.getWaitingPlayer().getMinions().get(0).getTotalHealth(), 1);
        assertEquals(ret0.data_.getWaitingPlayer().getMinions().get(1).getTotalHealth(), 4);
        assertEquals(ret0.data_.getWaitingPlayer().getMinions().get(2).getTotalHealth(), 2);
        assertEquals(ret0.data_.getWaitingPlayer().getMinions().get(3).getTotalHealth(), 7);

        assertEquals(ret0.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 4);
        assertEquals(ret0.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 3);
        assertEquals(ret0.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 7);
        assertEquals(ret0.data_.getCurrentPlayer().getMinions().get(3).getTotalAttack(), 4);

        assertEquals(ret0.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 3);
        assertEquals(ret0.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 5);
        assertEquals(ret0.data_.getWaitingPlayer().getMinions().get(2).getTotalAttack(), 2);
        assertEquals(ret0.data_.getWaitingPlayer().getMinions().get(3).getTotalAttack(), 7);

        //------------------------------------------------------------------
        //------------------------------------------------------------------

        HearthTreeNode ret1 = ret.getChildren().get(1);
        assertEquals(ret1.data_.getCurrentPlayer().getHand().size(), 0);
        assertEquals(ret1.data_.getCurrentPlayer().getNumMinions(), 4);
        assertEquals(ret1.data_.getWaitingPlayer().getNumMinions(), 4);
        assertEquals(ret1.data_.getCurrentPlayer().getMana(), 8);
        assertEquals(ret1.data_.getWaitingPlayer().getMana(), 10);
        assertEquals(ret1.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(ret1.data_.getWaitingPlayer().getHero().getHealth(), 30);

        assertEquals(ret1.data_.getCurrentPlayer().getMinions().get(0).getTotalHealth(), 4);
        assertEquals(ret1.data_.getCurrentPlayer().getMinions().get(1).getTotalHealth(), 3);
        assertEquals(ret1.data_.getCurrentPlayer().getMinions().get(2).getTotalHealth(), 6);
        assertEquals(ret1.data_.getCurrentPlayer().getMinions().get(3).getTotalHealth(), 2);

        assertEquals(ret1.data_.getWaitingPlayer().getMinions().get(0).getTotalHealth(), 1);
        assertEquals(ret1.data_.getWaitingPlayer().getMinions().get(1).getTotalHealth(), 4);
        assertEquals(ret1.data_.getWaitingPlayer().getMinions().get(2).getTotalHealth(), 2);
        assertEquals(ret1.data_.getWaitingPlayer().getMinions().get(3).getTotalHealth(), 7);

        assertEquals(ret1.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 3);
        assertEquals(ret1.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 3);
        assertEquals(ret1.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 6);
        assertEquals(ret1.data_.getCurrentPlayer().getMinions().get(3).getTotalAttack(), 3);

        assertEquals(ret1.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 3);
        assertEquals(ret1.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 5);
        assertEquals(ret1.data_.getWaitingPlayer().getMinions().get(2).getTotalAttack(), 2);
        assertEquals(ret1.data_.getWaitingPlayer().getMinions().get(3).getTotalAttack(), 7);

        //------------------------------------------------------------------
        //------------------------------------------------------------------

        HearthTreeNode ret2 = ret.getChildren().get(2);
        assertEquals(ret2.data_.getCurrentPlayer().getHand().size(), 0);
        assertEquals(ret2.data_.getCurrentPlayer().getNumMinions(), 4);
        assertEquals(ret2.data_.getWaitingPlayer().getNumMinions(), 4);
        assertEquals(ret2.data_.getCurrentPlayer().getMana(), 8);
        assertEquals(ret2.data_.getWaitingPlayer().getMana(), 10);
        assertEquals(ret2.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(ret2.data_.getWaitingPlayer().getHero().getHealth(), 30);

        assertEquals(ret2.data_.getCurrentPlayer().getMinions().get(0).getTotalHealth(), 3);
        assertEquals(ret2.data_.getCurrentPlayer().getMinions().get(1).getTotalHealth(), 2);
        assertEquals(ret2.data_.getCurrentPlayer().getMinions().get(2).getTotalHealth(), 6);
        assertEquals(ret2.data_.getCurrentPlayer().getMinions().get(3).getTotalHealth(), 1);

        assertEquals(ret2.data_.getWaitingPlayer().getMinions().get(0).getTotalHealth(), 1);
        assertEquals(ret2.data_.getWaitingPlayer().getMinions().get(1).getTotalHealth(), 4);
        assertEquals(ret2.data_.getWaitingPlayer().getMinions().get(2).getTotalHealth(), 2);
        assertEquals(ret2.data_.getWaitingPlayer().getMinions().get(3).getTotalHealth(), 7);

        assertEquals(ret2.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 3);
        assertEquals(ret2.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 2);
        assertEquals(ret2.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 7);
        assertEquals(ret2.data_.getCurrentPlayer().getMinions().get(3).getTotalAttack(), 3);

        assertEquals(ret2.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 3);
        assertEquals(ret2.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 5);
        assertEquals(ret2.data_.getWaitingPlayer().getMinions().get(2).getTotalAttack(), 2);
        assertEquals(ret2.data_.getWaitingPlayer().getMinions().get(3).getTotalAttack(), 7);

        //------------------------------------------------------------------
        //------------------------------------------------------------------
    }
}
