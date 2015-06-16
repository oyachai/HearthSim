package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.AcidicSwampOoze;
import com.hearthsim.card.basic.weapon.FieryWarAxe;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestAcidicSwampOoze {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        AcidicSwampOoze ooze = new AcidicSwampOoze();
        currentPlayer.placeCardHand(ooze);

        currentPlayer.setMana((byte) 4);
        waitingPlayer.setMana((byte) 4);
    }

    @Test
    public void testDestroysWeapon() throws HSException {
        FieryWarAxe axe = new FieryWarAxe();
        waitingPlayer.getHero().setWeapon(axe);

        assertEquals(currentPlayer.getHero().getTotalAttack(), 0);
        assertNull(currentPlayer.getHero().getWeapon());
        assertEquals(waitingPlayer.getHero().getWeapon(), axe);
        assertEquals(waitingPlayer.getHero().getTotalAttack(), 3);
        assertEquals(waitingPlayer.getHero().getWeapon().getWeaponCharge(), 2);

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);

        assertNotNull(ret);
        currentPlayer = ret.data_.getCurrentPlayer();
        waitingPlayer = ret.data_.getWaitingPlayer();

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 1);

        assertNull(currentPlayer.getHero().getWeapon());
        assertEquals(currentPlayer.getHero().getTotalAttack(), 0);
        assertNull(waitingPlayer.getHero().getWeapon());
        assertEquals(waitingPlayer.getHero().getTotalAttack(), 0);
    }
}
