package com.hearthsim.test.minion;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.BoulderfistOgre;
import com.hearthsim.card.basic.minion.Windspeaker;
import com.hearthsim.card.classic.minion.common.DustDevil;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestWindspeaker {
    private HearthTreeNode board;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
    }

    @Test
    public void testAddsWindfury() throws HSException {
        BoulderfistOgre ogre = new BoulderfistOgre();
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, ogre);
        Windspeaker windspeaker = new Windspeaker();
        windspeaker.getBattlecryEffect().applyEffect(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1, board);
        assertTrue(ogre.getWindfury());
    }

    @Test
    @Ignore("Unverified behavior")
    // TODO verify behavior
    public void testCannotTargetWindfury() throws HSException {
        Windspeaker windspeaker = new Windspeaker();
        board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, windspeaker);

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BoulderfistOgre());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new DustDevil());

        HearthTreeNode ret = windspeaker.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        assertEquals(board.numChildren(), 1);
    }}
