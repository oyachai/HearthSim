package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.basic.minion.BoulderfistOgre;
import com.hearthsim.card.basic.minion.RaidLeader;
import com.hearthsim.card.basic.minion.WaterElemental;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestWaterElemental {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        Card fb = new WaterElemental();
        currentPlayer.placeCardHand(fb);

        currentPlayer.setMana((byte) 8);
        currentPlayer.setMaxMana((byte) 8);

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new BoulderfistOgre());
    }

    @Test
    public void testFreezesMinionOnAttack() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board);
        assertEquals(board, ret);

        Minion waterElemental = currentPlayer.getCharacter(1);
        assertTrue(waterElemental instanceof WaterElemental);

        waterElemental.hasAttacked(false); // unset summoning sickness
        waterElemental.attack(PlayerSide.WAITING_PLAYER, 2, board, false);
        assertTrue(waitingPlayer.getCharacter(2).getFrozen());
    }

    @Test
    public void testFreezesHeroOnAttack() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board);
        assertEquals(board, ret);

        Minion waterElemental = currentPlayer.getCharacter(1);
        assertTrue(waterElemental instanceof WaterElemental);

        waterElemental.hasAttacked(false); // unset summoning sickness
        waterElemental.attack(PlayerSide.WAITING_PLAYER, 0, board, false);
        assertTrue(waitingPlayer.getCharacter(0).getFrozen());
    }
}
