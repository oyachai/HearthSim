package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BloodKnight;
import com.hearthsim.card.minion.concrete.ScarletCrusader;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestBloodKnight {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        Minion minion0_0 = new ScarletCrusader();
        Minion minion1_0 = new ScarletCrusader();

        currentPlayer.placeCardHand(new BloodKnight());

        currentPlayer.setMana((byte) 18);
        waitingPlayer.setMana((byte) 18);

        minion0_0.summonMinion(PlayerSide.CURRENT_PLAYER, currentPlayer.getHero(), board, false, true);
        minion1_0.summonMinion(PlayerSide.WAITING_PLAYER, waitingPlayer.getHero(), board, false, true);
    }

    @Test
    public void testStealsDivineShieldMultiple() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board);

        assertEquals(board, ret);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 15);

        assertEquals(currentPlayer.getMinions().get(1).getTotalHealth(), 9);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 9);

        assertFalse(currentPlayer.getMinions().get(0).getDivineShield());
        assertFalse(waitingPlayer.getMinions().get(0).getDivineShield());
    }

    @Test
    public void testStealsDivineShieldSingle() throws HSException {
        Minion target = currentPlayer.getCharacter(1);
        target.setDivineShield(false);
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, target, board);

        assertEquals(board, ret);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 15);

        assertEquals(currentPlayer.getMinions().get(1).getTotalHealth(), 6);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 6);

        assertFalse(currentPlayer.getMinions().get(0).getDivineShield());
        assertFalse(waitingPlayer.getMinions().get(0).getDivineShield());
    }

    @Test
    public void testNoDivineShields() throws HSException {
        currentPlayer.getCharacter(1).setDivineShield(false);
        waitingPlayer.getCharacter(1).setDivineShield(false);
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board);

        assertEquals(board, ret);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 15);

        assertEquals(currentPlayer.getMinions().get(1).getTotalHealth(), 3);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 3);

        assertFalse(currentPlayer.getMinions().get(0).getDivineShield());
        assertFalse(waitingPlayer.getMinions().get(0).getDivineShield());
    }

    @Test
    @Ignore("Existing bug")
    public void testSilenced() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board);
        assertEquals(board, ret);

        Minion target = currentPlayer.getCharacter(2);
        target.silenced(PlayerSide.CURRENT_PLAYER, board);

        assertEquals(currentPlayer.getMinions().get(1).getTotalHealth(), 3);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 3);

        assertFalse(currentPlayer.getMinions().get(0).getDivineShield());
        assertFalse(waitingPlayer.getMinions().get(0).getDivineShield());
    }
}
