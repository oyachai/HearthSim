package com.hearthsim.test.spell;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.spell.AncestralHealing;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionMock;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestAncestralHealing {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;

    private static final byte mana = 2;
    private static final byte attack0 = 2;
    private static final byte health0 = 5;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        Minion minion0 = new MinionMock("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion1 = new MinionMock("" + 0, mana, attack0, health0, attack0, health0, health0);

        AncestralHealing fb = new AncestralHealing();
        currentPlayer.placeCardHand(fb);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1);
        currentPlayer.setMana((byte) 2);
    }

    @Test
    public void test0() throws HSException {

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode res;

        try {
            res = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
            assertNull(res);
        } catch (HSInvalidPlayerIndexException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        try {
            res = theCard.useOn(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);
            assertNull(res);
        } catch (HSInvalidPlayerIndexException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void test1() throws HSException {

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode res;

        try {
            res = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1, board);
            assertNotNull(res);
            assertEquals(res.data_.getCurrentPlayer().getMana(), 2);
            assertEquals(res.data_.getCurrentPlayer().getHand().size(), 0);
            assertEquals(res.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
            assertEquals(res.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getTotalAttack(), attack0);
            assertEquals(res.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getMaxHealth(), health0);
            assertTrue(res.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getTaunt());
        } catch (HSInvalidPlayerIndexException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void test2() throws HSException {

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode res;

        try {
            res = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1, board);
            assertNotNull(res);
            assertEquals(res.data_.getCurrentPlayer().getMana(), 2);
            assertEquals(res.data_.getCurrentPlayer().getHand().size(), 0);
            assertEquals(res.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
            assertEquals(res.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getTotalAttack(), attack0);
            assertEquals(res.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getMaxHealth(), health0);
            assertTrue(res.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getTaunt());
        } catch (HSInvalidPlayerIndexException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void test3() throws HSException {

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode res;

        try {
            res = theCard.useOn(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_1, board);
            assertNotNull(res);
            assertEquals(res.data_.getCurrentPlayer().getMana(), 2);
            assertEquals(res.data_.getCurrentPlayer().getHand().size(), 0);
            assertEquals(res.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
            assertEquals(res.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).getTotalAttack(), attack0);
            assertEquals(res.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).getMaxHealth(), health0);
            assertTrue(res.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).getTaunt());
        } catch (HSInvalidPlayerIndexException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void test4() throws HSException {

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode res;

        try {
            res = theCard.useOn(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_1, board);
            assertNotNull(res);
            assertEquals(res.data_.getCurrentPlayer().getMana(), 2);
            assertEquals(res.data_.getCurrentPlayer().getHand().size(), 0);
            assertEquals(res.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
            assertEquals(res.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).getTotalAttack(), attack0);
            assertEquals(res.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).getMaxHealth(), health0);
            assertTrue(res.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).getTaunt());
        } catch (HSInvalidPlayerIndexException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
}
