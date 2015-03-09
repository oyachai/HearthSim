package com.hearthsim.test.minion;

import static org.junit.Assert.assertEquals;

import com.hearthsim.model.PlayerModel;
import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.DarkIronDwarf;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestDarkIronDwarf {
    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();
    }

    @Test
    public void testAddsExtraAttack() throws HSException {
        BoulderfistOgre ogre = new BoulderfistOgre();
        DarkIronDwarf darkIronDwarf = new DarkIronDwarf();
        darkIronDwarf.useTargetableBattlecry_core(PlayerSide.WAITING_PLAYER, ogre, board, null, null);
        assertEquals(2, ogre.getExtraAttackUntilTurnEnd());
    }

    @Test
    public void testBuffIsAdditive() throws HSException {
        BoulderfistOgre ogre = new BoulderfistOgre();
        ogre.setExtraAttackUntilTurnEnd((byte)2);
        DarkIronDwarf darkIronDwarf = new DarkIronDwarf();
        darkIronDwarf.useTargetableBattlecry_core(PlayerSide.WAITING_PLAYER, ogre, board, null, null);
        assertEquals(4, ogre.getExtraAttackUntilTurnEnd());
    }
}
