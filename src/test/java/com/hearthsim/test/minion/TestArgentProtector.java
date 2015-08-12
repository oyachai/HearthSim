package com.hearthsim.test.minion;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.BoulderfistOgre;
import com.hearthsim.card.classic.minion.common.ArgentProtector;
import com.hearthsim.card.classic.minion.common.ArgentSquire;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestArgentProtector {
    private HearthTreeNode board;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
    }

    @Test
    public void testAddsDivineShield() throws HSException {
        BoulderfistOgre ogre = new BoulderfistOgre();
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, ogre);
        ArgentProtector protector = new ArgentProtector();
        protector.getBattlecryEffect().applyEffect(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1, board);
        assertTrue(ogre.getDivineShield());
    }

    @Test
    @Ignore("Unverified behavior")
    // TODO verify behavior
    public void testCannotTargetDivineShield() throws HSException {
        ArgentProtector protector = new ArgentProtector();
        board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, protector);

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BoulderfistOgre());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new ArgentSquire());

        HearthTreeNode ret = protector.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        assertEquals(board.numChildren(), 1);
    }
}
