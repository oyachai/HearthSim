package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.BloodfenRaptor;
import com.hearthsim.card.basic.minion.Houndmaster;
import com.hearthsim.card.basic.minion.StonetuskBoar;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionMock;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestHoundmaster {

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
        Minion minion1 = new StonetuskBoar();
        Minion minion2 = new MinionMock("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion3 = new BloodfenRaptor();
        Minion minion4 = new MinionMock("" + 0, mana, attack0, (byte)(health0-2), attack0, health0, health0);

        Houndmaster fb = new Houndmaster();
        currentPlayer.placeCardHand(fb);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion1);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion2);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion3);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion4);

        currentPlayer.setMana((byte) 10);
    }

    @Test
    public void test_deepCopy() {

        Minion card1 = new Houndmaster();
        Minion card1_cloned = (Minion)card1.deepCopy();

        assertTrue(card1.equals(card1_cloned));
        assertTrue(card1_cloned.equals(card1));

        card1.setHealth((byte)(card1.getHealth() + 1));
        assertFalse(card1.equals(card1_cloned));
        assertFalse(card1_cloned.equals(card1));

        card1_cloned = (Minion)card1.deepCopy();
        assertTrue(card1.equals(card1_cloned));
        assertTrue(card1_cloned.equals(card1));

        card1.setAttack((byte)(card1.getTotalAttack() + 1));
        assertFalse(card1.equals(card1_cloned));
        assertFalse(card1_cloned.equals(card1));

        card1_cloned = (Minion)card1.deepCopy();
        assertTrue(card1.equals(card1_cloned));
        assertTrue(card1_cloned.equals(card1));

        card1.setDestroyOnTurnEnd(true);
        assertFalse(card1.equals(card1_cloned));
        assertFalse(card1_cloned.equals(card1));
        card1_cloned = (Minion)card1.deepCopy();
        assertTrue(card1.equals(card1_cloned));
        assertTrue(card1_cloned.equals(card1));

        card1.setDestroyOnTurnStart(true);
        assertFalse(card1.equals(card1_cloned));
        assertFalse(card1_cloned.equals(card1));
        card1_cloned = (Minion)card1.deepCopy();
        assertTrue(card1.equals(card1_cloned));
        assertTrue(card1_cloned.equals(card1));
    }
    @Test
    public void test0() throws HSException {

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode res;

        res = theCard.useOn(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);
        assertNull(res);

        res = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertNotNull(res);
        assertEquals(res.data_.getCurrentPlayer().getHand().size(), 0);
        assertEquals(res.data_.getCurrentPlayer().getNumMinions(), 3);
        assertEquals(res.data_.getWaitingPlayer().getNumMinions(), 3);
        assertEquals(res.data_.getCurrentPlayer().getMana(), 6);

        assertEquals(res.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getHealth(), 3);
        assertEquals(res.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 4);
        assertFalse(res.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_1).getTaunt());
        assertEquals(res.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_2).getHealth(), health0);
        assertEquals(res.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_2).getTotalAttack(), attack0);
        assertFalse(res.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_2).getTaunt());
        assertEquals(res.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_3).getHealth(), 1 + 2);
        assertEquals(res.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 1 + 2);
        assertTrue(res.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_3).getTaunt());

        assertEquals(res.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(res.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).getTotalAttack(), attack0);
        assertFalse(res.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).getTaunt());
        assertEquals(res.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_2).getHealth(), 2);
        assertEquals(res.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 3);
        assertFalse(res.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_2).getTaunt());
        assertEquals(res.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_3).getHealth(), health0-2);
        assertEquals(res.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_3).getTotalAttack(), attack0);
        assertFalse(res.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_3).getTaunt());

        assertEquals(res.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(res.data_.getWaitingPlayer().getHero().getHealth(), 30);
    }
}
