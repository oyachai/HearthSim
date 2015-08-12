package com.hearthsim.test.minion;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.BoulderfistOgre;
import com.hearthsim.card.classic.minion.common.DarkIronDwarf;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestDarkIronDwarf {
    private HearthTreeNode board;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
    }

    @Test
    public void testAddsExtraAttack() throws HSException {
        BoulderfistOgre ogre = new BoulderfistOgre();
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, ogre);

        DarkIronDwarf darkIronDwarf = new DarkIronDwarf();
        darkIronDwarf.getBattlecryEffect().applyEffect(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1, board);
        assertEquals(2, ogre.getExtraAttackUntilTurnEnd());
    }

    @Test
    public void testBuffIsAdditive() throws HSException {
        BoulderfistOgre ogre = new BoulderfistOgre();
        ogre.setExtraAttackUntilTurnEnd((byte)2);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, ogre);

        DarkIronDwarf darkIronDwarf = new DarkIronDwarf();
        darkIronDwarf.getBattlecryEffect().applyEffect(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1, board);
        assertEquals(4, ogre.getExtraAttackUntilTurnEnd());
    }
}
