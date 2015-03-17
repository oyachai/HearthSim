package com.hearthsim.test.minion;

import static org.junit.Assert.assertEquals;

import com.hearthsim.model.PlayerModel;
import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.concrete.ArathiWeaponsmith;
import com.hearthsim.card.weapon.concrete.FieryWarAxe;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestArathiWeaponsmith {
    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        currentPlayer.setMana((byte) 10);
        waitingPlayer.setMana((byte) 10);

        currentPlayer.placeCardHand(new ArathiWeaponsmith());
    }

    @Test
    public void testEquipsWeapon() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 1);

        //should be equipped with a weapon now
        assertEquals(currentPlayer.getHero().getWeapon().getWeaponCharge(), 2);
        assertEquals(currentPlayer.getHero().getTotalAttack(), 2);
    }

    @Test
    public void testDestroysExistingWeapon() throws HSException {
        currentPlayer.placeCardHand(new FieryWarAxe());
        currentPlayer.getHand().get(1).useOn(PlayerSide.CURRENT_PLAYER, 0, board);

        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getWeapon().getWeaponCharge(), 2);
        assertEquals(currentPlayer.getHero().getTotalAttack(), 3);

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board);
        assertEquals(board, ret);
        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 1);

        //should be equipped with a weapon now
        assertEquals(currentPlayer.getHero().getWeapon().getWeaponCharge(), 2);
        assertEquals(currentPlayer.getHero().getTotalAttack(), 2);
    }
}
