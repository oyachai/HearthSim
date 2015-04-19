package com.hearthsim.test;

import com.hearthsim.card.basic.minion.BloodfenRaptor;
import com.hearthsim.card.basic.minion.ChillwindYeti;
import com.hearthsim.card.basic.minion.RiverCrocolisk;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestMinionDamage {

    private HearthTreeNode board;

    private RiverCrocolisk croc;

    @Before
    public void setUp() throws Exception {
        board = new HearthTreeNode(new BoardModel());
        PlayerModel currentPlayer = board.data_.getCurrentPlayer();

        BloodfenRaptor raptor = new BloodfenRaptor();
        ChillwindYeti yeti = new ChillwindYeti();
        croc = new RiverCrocolisk();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, raptor);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, yeti);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, croc);

        currentPlayer.setMana((byte) 8);
    }

    @Test
    public void testImmunity() throws HSException {
        croc.setImmune(true);
        HearthTreeNode ret = croc.takeDamageAndNotify((byte) 2, PlayerSide.CURRENT_PLAYER, PlayerSide.WAITING_PLAYER, board, false, false);
        assertEquals(board, ret);

        assertTrue(croc.getImmune());
        assertEquals(3, croc.getHealth());
    }
}
