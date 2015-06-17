package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.weapon.FieryWarAxe;
import com.hearthsim.card.classic.minion.common.ArathiWeaponsmith;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestArathiWeaponsmith {
    private HearthTreeNode board;
    private PlayerModel currentPlayer;

    @Before
    public void setup() {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        PlayerModel waitingPlayer = board.data_.getWaitingPlayer();

        currentPlayer.setMana((byte) 10);
        waitingPlayer.setMana((byte) 10);

        currentPlayer.placeCardHand(new ArathiWeaponsmith());
    }

    @Test
    public void testEquipsWeapon() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertNotNull(ret);
        currentPlayer = ret.data_.getCurrentPlayer();

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 1);

        //should be equipped with a weapon now
        assertEquals(currentPlayer.getHero().getWeapon().getWeaponCharge(), 2);
        assertEquals(currentPlayer.getHero().getTotalAttack(), 2);
    }

    @Test
    public void testDestroysExistingWeapon() throws HSException {
        currentPlayer.placeCardHand(new FieryWarAxe());
        currentPlayer.getHand().get(1).useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);

        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getWeapon().getWeaponCharge(), 2);
        assertEquals(currentPlayer.getHero().getTotalAttack(), 3);

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertNotNull(ret);
        currentPlayer = ret.data_.getCurrentPlayer();

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 1);

        //should be equipped with a weapon now
        assertEquals(currentPlayer.getHero().getWeapon().getWeaponCharge(), 2);
        assertEquals(currentPlayer.getHero().getTotalAttack(), 2);
    }
}
