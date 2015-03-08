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

    @Before
    public void setup() {
        board = new HearthTreeNode(new BoardModel());

        board.data_.getCurrentPlayer().setMana((byte)10);
        board.data_.getWaitingPlayer().setMana((byte)10);

        board.data_.getCurrentPlayer().setMaxMana((byte)10);
        board.data_.getWaitingPlayer().setMaxMana((byte)10);

        board.data_.placeCardHandCurrentPlayer(new ArathiWeaponsmith());
    }

    @Test
    public void testEquipsWeapon() throws HSException {
        Card theCard = board.data_.getCurrentPlayerCardHand(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
        assertEquals(board, ret);
        PlayerModel currentPlayer = board.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = board.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        assertEquals(board.data_.getNumCards_hand(), 0);
        assertEquals(currentPlayer.getNumMinions(), 1);

        //should be equipped with a weapon now
        assertEquals(currentPlayer.getHero().getWeapon().getWeaponCharge(), 2);
        assertEquals(currentPlayer.getHero().getTotalAttack(), 2);
    }

    @Test
    public void testDestroysExistingWeapon() throws HSException {
        board.data_.placeCardHandCurrentPlayer(new FieryWarAxe());
        board.data_.getCurrentPlayerCardHand(1).useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
        PlayerModel currentPlayer = board.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = board.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        assertEquals(board.data_.getCurrentPlayer().getMana(), 8);
        assertEquals(board.data_.getCurrentPlayer().getHero().getWeapon().getWeaponCharge(), 2);
        assertEquals(board.data_.getCurrentPlayer().getHero().getTotalAttack(), 3);

        Card theCard = board.data_.getCurrentPlayerCardHand(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
        assertEquals(board, ret);
        assertEquals(board.data_.getNumCards_hand(), 0);
        assertEquals(currentPlayer.getNumMinions(), 1);

        //should be equipped with a weapon now
        assertEquals(currentPlayer.getHero().getWeapon().getWeaponCharge(), 2);
        assertEquals(currentPlayer.getHero().getTotalAttack(), 2);
    }
}
