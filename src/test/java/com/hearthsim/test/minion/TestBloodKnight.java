package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.classic.minion.common.ScarletCrusader;
import com.hearthsim.card.classic.minion.epic.BloodKnight;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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

        minion0_0.summonMinion(PlayerSide.CURRENT_PLAYER, currentPlayer.getHero(), board, false);
        minion1_0.summonMinion(PlayerSide.WAITING_PLAYER, waitingPlayer.getHero(), board, false);
    }

    @Test
    public void testStealsDivineShieldMultiple() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1, board);

        assertNotNull(ret);
        currentPlayer = ret.data_.getCurrentPlayer();
        waitingPlayer = ret.data_.getWaitingPlayer();

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 15);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 9);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 9);

        assertFalse(currentPlayer.getCharacter(CharacterIndex.MINION_1).getDivineShield());
        assertFalse(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getDivineShield());
    }

    @Test
    public void testStealsDivineShieldSingle() throws HSException {
        Minion target = currentPlayer.getCharacter(CharacterIndex.MINION_1);
        target.setDivineShield(false);
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1, board);

        assertNotNull(ret);
        currentPlayer = ret.data_.getCurrentPlayer();
        waitingPlayer = ret.data_.getWaitingPlayer();

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 15);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 6);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 6);

        assertFalse(currentPlayer.getCharacter(CharacterIndex.MINION_1).getDivineShield());
        assertFalse(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getDivineShield());
    }

    @Test
    public void testNoDivineShields() throws HSException {
        currentPlayer.getCharacter(CharacterIndex.MINION_1).setDivineShield(false);
        waitingPlayer.getCharacter(CharacterIndex.MINION_1).setDivineShield(false);
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1, board);

        assertNotNull(ret);
        currentPlayer = ret.data_.getCurrentPlayer();
        waitingPlayer = ret.data_.getWaitingPlayer();

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 15);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 3);

        assertFalse(currentPlayer.getCharacter(CharacterIndex.MINION_1).getDivineShield());
        assertFalse(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getDivineShield());
    }

    @Test
    public void testSilenced() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1, board);
        assertNotNull(ret);
        currentPlayer = ret.data_.getCurrentPlayer();
        waitingPlayer = ret.data_.getWaitingPlayer();

        Minion target = currentPlayer.getCharacter(CharacterIndex.MINION_2);
        target.silenced(PlayerSide.CURRENT_PLAYER, ret.data_);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalHealth(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 3);

        assertFalse(currentPlayer.getCharacter(CharacterIndex.MINION_1).getDivineShield());
        assertFalse(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getDivineShield());
    }
}
