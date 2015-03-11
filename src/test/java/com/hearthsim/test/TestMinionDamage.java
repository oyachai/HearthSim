package com.hearthsim.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.hearthsim.model.PlayerModel;
import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.ChillwindYeti;
import com.hearthsim.card.minion.concrete.RiverCrocolisk;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestMinionDamage {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    private BloodfenRaptor raptor;
    private ChillwindYeti yeti;
    private RiverCrocolisk croc;

    @Before
    public void setUp() throws Exception {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        raptor = new BloodfenRaptor();
        yeti = new ChillwindYeti();
        croc = new RiverCrocolisk();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, raptor);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, yeti);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, croc);

        currentPlayer.setMana((byte) 8);
    }

    @Test
    public void testImmunity() throws HSException {
        croc.setImmune(true);
        HearthTreeNode ret = croc.takeDamage((byte)2, PlayerSide.CURRENT_PLAYER, PlayerSide.WAITING_PLAYER, board, false, false);
        assertEquals(board, ret);

        assertTrue(croc.getImmune());
        assertEquals(3, croc.getHealth());
    }
}
