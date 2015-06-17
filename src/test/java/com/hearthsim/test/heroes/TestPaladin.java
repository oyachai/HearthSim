package com.hearthsim.test.heroes;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.BoulderfistOgre;
import com.hearthsim.card.basic.minion.RaidLeader;
import com.hearthsim.card.basic.minion.SilverHandRecruit;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.heroes.Paladin;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestPaladin {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel(new Paladin(), new TestHero()));
        currentPlayer = board.data_.getCurrentPlayer();
        PlayerModel waitingPlayer = board.data_.getWaitingPlayer();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BoulderfistOgre());

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new BoulderfistOgre());

        currentPlayer.setMana((byte) 8);
        waitingPlayer.setMana((byte) 8);
    }

    @Test
    public void testHeropower() throws HSException {
        Hero paladin = currentPlayer.getHero();

        HearthTreeNode ret = paladin.useHeroAbility(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(currentPlayer.getMana(), 6);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 7);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getHealth(), 1);

        // buffed by Raid Leader
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), 2);
    }

    @Test
    public void testHeropowerWithFullBoard() throws HSException {
        Hero paladin = currentPlayer.getHero();
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
        assertEquals(currentPlayer.getNumMinions(), 7);

        HearthTreeNode ret = paladin.useHeroAbility(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertNull(ret);

        assertEquals(currentPlayer.getNumMinions(), 7);
        assertEquals(currentPlayer.getMana(), 8);
    }

    @Test
    public void testHeropowerCannotTargetMinion() throws HSException {
        Hero paladin = currentPlayer.getHero();

        HearthTreeNode ret = paladin.useHeroAbility(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_2, board);
        assertNull(ret);

        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(currentPlayer.getNumMinions(), 2);
    }

    @Test
    public void testHeropowerCannotTargetOpponent() throws HSException {
        Hero paladin = currentPlayer.getHero();

        HearthTreeNode ret = paladin.useHeroAbility(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);
        assertNull(ret);

        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(currentPlayer.getNumMinions(), 2);
    }
}
