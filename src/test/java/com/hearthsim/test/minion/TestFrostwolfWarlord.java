package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.FrostwolfWarlord;
import com.hearthsim.card.basic.minion.SilverHandRecruit;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestFrostwolfWarlord {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;

    private FrostwolfWarlord warlord;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();

        warlord = new FrostwolfWarlord();
        currentPlayer.placeCardHand(warlord);

        currentPlayer.setMana((byte) 7);
        currentPlayer.setMaxMana((byte) 7);
    }

    @Test
    public void testBattlecryWithMultipleMinions() throws HSException {
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1, board);
        assertNotNull(ret);
        currentPlayer = ret.data_.getCurrentPlayer();

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(currentPlayer.getMana(), 2);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 4 + 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getAttack(), 4 + 2);
    }

    @Test
    public void testBattlecryWithNoMinions() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertNotNull(ret);
        currentPlayer = ret.data_.getCurrentPlayer();

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 1);
        assertEquals(currentPlayer.getMana(), 2);

        assertEquals(warlord.getHealth(), 4);
        assertEquals(warlord.getAttack(), 4);
    }

    @Test
    public void testSilence() throws HSException {
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertNotNull(ret);
        currentPlayer = ret.data_.getCurrentPlayer();

        warlord.silenced(PlayerSide.CURRENT_PLAYER, board);

        assertEquals(warlord.getHealth(), 4);
        assertEquals(warlord.getAttack(), 4);
    }
}
