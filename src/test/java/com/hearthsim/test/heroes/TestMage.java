package com.hearthsim.test.heroes;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.BoulderfistOgre;
import com.hearthsim.card.basic.minion.ChillwindYeti;
import com.hearthsim.card.classic.minion.common.FaerieDragon;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.heroes.Mage;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestMage {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel(new Mage(), new TestHero()));
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new ChillwindYeti());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BoulderfistOgre());

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new ChillwindYeti());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new BoulderfistOgre());

        currentPlayer.setMana((byte) 8);
        waitingPlayer.setMana((byte) 8);
    }

    @Test
    public void testHeropowerAgainstOpponent() throws HSException {
        Hero mage = currentPlayer.getHero();

        HearthTreeNode ret = mage.useHeroAbility(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getMana(), 6);
        assertEquals(waitingPlayer.getHero().getHealth(), 29);
    }

    @Test
    public void testHeropowerAgainstMinion() throws HSException {
        Hero mage = currentPlayer.getHero();

        HearthTreeNode ret = mage.useHeroAbility(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_1, board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getMana(), 6);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 5);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 7);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 4);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 7);
    }

    @Test
    @Ignore("Functionality explicitly disabled")
    public void testHeropowerAgainstSelf() throws HSException {
        Hero mage = currentPlayer.getHero();

        HearthTreeNode ret = mage.useHeroAbility(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getMana(), 6);
        assertEquals(currentPlayer.getHero().getHealth(), 29);
    }

    @Test
    public void testHeropowerAgainstOwnMinion() throws HSException {
        Hero mage = currentPlayer.getHero();

        HearthTreeNode ret = mage.useHeroAbility(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1, board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getMana(), 6);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 4);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 7);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 5);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 7);
    }

    @Test
    public void testHeropowerAgainstFaerieMinion() throws HSException {
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new FaerieDragon());

        Hero mage = currentPlayer.getHero();

        assertFalse(mage.canBeUsedOn(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_3, board.data_));
        HearthTreeNode ret = mage.useHeroAbility(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_3, board);
        assertNull(ret);

        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_3).getHealth(), 2);
    }

    @Test
    public void testHeropowerKillsMinion() throws HSException {
        Minion target = waitingPlayer.getCharacter(CharacterIndex.MINION_1); // Yeti
        target.setHealth((byte)1);

        Hero mage = currentPlayer.getHero();

        HearthTreeNode ret = mage.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 1);

        assertEquals(currentPlayer.getMana(), 6);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 5);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 7);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 7);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 4);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 6);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 6);
    }
}
