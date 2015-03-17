package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.*;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestCrazedAlchemist {

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

        Minion fb = new CrazedAlchemist();
        currentPlayer.placeCardHand(fb);
    }

    @Test
    public void test0() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 0, board);

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

        //set the remaining total health of Abomination to 1
        waitingPlayer.getMinions().get(1).setHealth((byte)1);

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 4);
        assertEquals(waitingPlayer.getNumMinions(), 4);
        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(waitingPlayer.getMana(), 10);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);

        assertEquals(currentPlayer.getMinions().get(0).getTotalHealth(), 3);
        assertEquals(currentPlayer.getMinions().get(1).getTotalHealth(), 4);
        assertEquals(currentPlayer.getMinions().get(2).getTotalHealth(), 3);
        assertEquals(currentPlayer.getMinions().get(3).getTotalHealth(), 6);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalHealth(), 1);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalHealth(), 1);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(3).getTotalHealth(), 7);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 4);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 4);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 3);
        assertEquals(currentPlayer.getMinions().get(3).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 3);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(3).getTotalAttack(), 7);

        assertEquals(ret.numChildren(), 7);

        //Forth child node is the one that hits the Loot Hoarder
        HearthTreeNode cn3 = ret.getChildren().get(3);
        assertEquals(cn3.data_.getCurrentPlayer().getHand().size(), 0);
        assertEquals(cn3.data_.getWaitingPlayer().getHand().size(), 0);
        assertEquals(cn3.data_.getCurrentPlayer().getNumMinions(), 4);
        assertEquals(cn3.data_.getWaitingPlayer().getNumMinions(), 4);
        assertEquals(cn3.data_.getCurrentPlayer().getMana(), 8);
        assertEquals(cn3.data_.getWaitingPlayer().getMana(), 10);
        assertEquals(cn3.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(cn3.data_.getWaitingPlayer().getHero().getHealth(), 30);

        assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(0).getTotalHealth(), 3);
        assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(1).getTotalHealth(), 4);
        assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(2).getTotalHealth(), 3);
        assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(3).getTotalHealth(), 6);
        assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(0).getTotalHealth(), 3);
        assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(1).getTotalHealth(), 1);
        assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(2).getTotalHealth(), 2);
        assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(3).getTotalHealth(), 7);

        assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 4);
        assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 4);
        assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 3);
        assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(3).getTotalAttack(), 7);
        assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 2);
        assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 5);
        assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(2).getTotalAttack(), 2);
        assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(3).getTotalAttack(), 7);

        //First child node is the one that hits the Harvest Golem
        HearthTreeNode cn2 = ret.getChildren().get(0);
        assertEquals(cn2.data_.getCurrentPlayer().getHand().size(), 0);
        assertEquals(cn2.data_.getWaitingPlayer().getHand().size(), 0);
        assertEquals(cn2.data_.getCurrentPlayer().getNumMinions(), 4);
        assertEquals(cn2.data_.getWaitingPlayer().getNumMinions(), 4);
        assertEquals(cn2.data_.getCurrentPlayer().getMana(), 8);
        assertEquals(cn2.data_.getWaitingPlayer().getMana(), 10);
        assertEquals(cn2.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(cn2.data_.getWaitingPlayer().getHero().getHealth(), 30);

        assertEquals(cn2.data_.getCurrentPlayer().getMinions().get(0).getTotalHealth(), 3);
        assertEquals(cn2.data_.getCurrentPlayer().getMinions().get(1).getTotalHealth(), 5);
        assertEquals(cn2.data_.getCurrentPlayer().getMinions().get(2).getTotalHealth(), 3);
        assertEquals(cn2.data_.getCurrentPlayer().getMinions().get(3).getTotalHealth(), 6);
        assertEquals(cn2.data_.getWaitingPlayer().getMinions().get(0).getTotalHealth(), 1);
        assertEquals(cn2.data_.getWaitingPlayer().getMinions().get(1).getTotalHealth(), 1);
        assertEquals(cn2.data_.getWaitingPlayer().getMinions().get(2).getTotalHealth(), 2);
        assertEquals(cn2.data_.getWaitingPlayer().getMinions().get(3).getTotalHealth(), 7);

        assertEquals(cn2.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 4);
        assertEquals(cn2.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 6);
        assertEquals(cn2.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 3);
        assertEquals(cn2.data_.getCurrentPlayer().getMinions().get(3).getTotalAttack(), 7);
        assertEquals(cn2.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 3);
        assertEquals(cn2.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 5);
        assertEquals(cn2.data_.getWaitingPlayer().getMinions().get(2).getTotalAttack(), 2);
        assertEquals(cn2.data_.getWaitingPlayer().getMinions().get(3).getTotalAttack(), 7);
    }
}
