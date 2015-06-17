package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.Nightblade;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestNightblade {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        Card fb = new Nightblade();
        currentPlayer.placeCardHand(fb);

        currentPlayer.setMana((byte) 10);
        currentPlayer.setMaxMana((byte) 7);
    }

    @Test
    public void testDamagesEnemyHero() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertNotNull(ret);
        currentPlayer = ret.data_.getCurrentPlayer();
        waitingPlayer = ret.data_.getWaitingPlayer();

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getMana(), 5);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 27);
    }

    @Test
    public void testKillsEnemyHero() throws HSException {
        waitingPlayer.getHero().setHealth((byte)2);

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertNotNull(ret);
        currentPlayer = ret.data_.getCurrentPlayer();
        waitingPlayer = ret.data_.getWaitingPlayer();

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getMana(), 5);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), -1);

        assertTrue(ret.data_.isLethalState());
    }
}
