package com.hearthsim.test.heroes;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.BoulderfistOgre;
import com.hearthsim.card.basic.minion.RaidLeader;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.heroes.Hunter;
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

public class TestHunter {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel(new Hunter(), new TestHero()));
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
        Hero hunter = currentPlayer.getHero();

        HearthTreeNode ret = hunter.useHeroAbility(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getMana(), 6);
        assertEquals(waitingPlayer.getHero().getHealth(), 28);
    }

    @Test
    public void testHeropowerCannotTargetMinion() throws HSException {
        Hero hunter = currentPlayer.getHero();

        HearthTreeNode ret = hunter.useHeroAbility(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_2, board);
        assertNull(ret);

        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 7);
    }

    @Test
    public void testHeropowerCannotTargetSelf() throws HSException {
        Hero hunter = currentPlayer.getHero();

        HearthTreeNode ret = hunter.useHeroAbility(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertNull(ret);

        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
    }
}
