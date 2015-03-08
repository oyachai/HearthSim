package com.hearthsim.test.minion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.hearthsim.model.PlayerModel;
import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.concrete.AcidicSwampOoze;
import com.hearthsim.card.weapon.concrete.FieryWarAxe;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestAcidicSwampOoze {

    private HearthTreeNode board;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());

        AcidicSwampOoze ooze = new AcidicSwampOoze();
        board.data_.placeCardHandCurrentPlayer(ooze);

        board.data_.getCurrentPlayer().setMana((byte)4);
        board.data_.getWaitingPlayer().setMana((byte)4);

        board.data_.getCurrentPlayer().setMaxMana((byte)4);
        board.data_.getWaitingPlayer().setMaxMana((byte)4);
    }

    @Test
    public void testDestroysWeapon() throws HSException {
        FieryWarAxe axe = new FieryWarAxe();
        board.data_.getWaitingPlayer().getHero().setWeapon(axe);

        assertEquals(board.data_.getCurrentPlayer().getHero().getTotalAttack(), 0);
        assertNull(board.data_.getCurrentPlayer().getHero().getWeapon());
        assertEquals(board.data_.getWaitingPlayer().getHero().getWeapon(), axe);
        assertEquals(board.data_.getWaitingPlayer().getHero().getTotalAttack(), 3);
        assertEquals(board.data_.getWaitingPlayer().getHero().getWeapon().getWeaponCharge(), 2);

        Card theCard = board.data_.getCurrentPlayer().getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);

        assertEquals(board, ret);
        PlayerModel currentPlayer = board.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = board.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        assertEquals(board.data_.getNumCards_hand(), 0);
        assertEquals(currentPlayer.getNumMinions(), 1);

        assertNull(board.data_.getCurrentPlayer().getHero().getWeapon());
        assertEquals(board.data_.getCurrentPlayer().getHero().getTotalAttack(), 0);
        assertNull(board.data_.getWaitingPlayer().getHero().getWeapon());
        assertEquals(board.data_.getWaitingPlayer().getHero().getTotalAttack(), 0);
    }
}
