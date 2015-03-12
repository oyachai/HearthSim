package com.hearthsim.test.heroes;

import com.hearthsim.model.PlayerModel;
import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.minion.concrete.SilverHandRecruit;
import com.hearthsim.card.minion.heroes.Paladin;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import static org.junit.Assert.*;

public class TestPaladin {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel(new Paladin(), new TestHero()));
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BoulderfistOgre());

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new BoulderfistOgre());

        currentPlayer.setMana((byte) 8);
        waitingPlayer.setMana((byte) 8);
    }

    @Test
    public void testHeropower() throws HSException {
        Minion target = currentPlayer.getCharacter(0);
        Hero paladin = currentPlayer.getHero();

        HearthTreeNode ret = paladin.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, null, null);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(currentPlayer.getMana(), 6);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 7);
        assertEquals(currentPlayer.getMinions().get(2).getHealth(), 1);

        // buffed by Raid Leader
        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 7);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 2);
    }

    @Test
    public void testHeropowerWithFullBoard() throws HSException {

        Minion target = currentPlayer.getCharacter(0);
        Hero paladin = currentPlayer.getHero();
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
        assertEquals(currentPlayer.getNumMinions(), 7);

        HearthTreeNode ret = paladin.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, null, null);
        assertNull(ret);

        assertEquals(currentPlayer.getNumMinions(), 7);
        assertEquals(currentPlayer.getMana(), 8);
    }

    @Test
    public void testHeropowerCannotTargetMinion() throws HSException {

        Minion target = waitingPlayer.getCharacter(2);
        Hero paladin = currentPlayer.getHero();

        HearthTreeNode ret = paladin.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, null, null);
        assertNull(ret);

        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(currentPlayer.getNumMinions(), 2);
    }

    @Test
    public void testHeropowerCannotTargetOpponent() throws HSException {

        Minion target = waitingPlayer.getCharacter(0);
        Hero paladin = currentPlayer.getHero();

        HearthTreeNode ret = paladin.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, null, null);
        assertNull(ret);

        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(currentPlayer.getNumMinions(), 2);
    }
}
