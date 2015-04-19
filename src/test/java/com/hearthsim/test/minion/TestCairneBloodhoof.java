package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.basic.minion.BloodfenRaptor;
import com.hearthsim.card.basic.minion.BoulderfistOgre;
import com.hearthsim.card.basic.minion.RaidLeader;
import com.hearthsim.card.basic.spell.Fireball;
import com.hearthsim.card.classic.minion.legendary.BaineBloodhoof;
import com.hearthsim.card.classic.minion.legendary.CairneBloodhoof;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestCairneBloodhoof {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BoulderfistOgre());

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new BoulderfistOgre());

        Card fb = new CairneBloodhoof();
        currentPlayer.placeCardHand(fb);

        currentPlayer.setMana((byte) 8);
        waitingPlayer.setMana((byte) 8);
    }

    @Test
    public void test0() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 0, board);

        assertNull(ret);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(2).getHealth(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getHealth(), 7);

        assertEquals(currentPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(2).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getTotalAttack(), 7);
    }

    @Test
    public void test2() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 2, board);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 2);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(2).getHealth(), 7);
        assertEquals(currentPlayer.getCharacter(3).getHealth(), 5);
        assertEquals(waitingPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getHealth(), 7);

        assertEquals(currentPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(2).getTotalAttack(), 7);
        assertEquals(currentPlayer.getCharacter(3).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getTotalAttack(), 7);

        //----------------------------------------------------------

        Minion minion = currentPlayer.getCharacter(3);
        assertTrue(minion instanceof CairneBloodhoof);

        minion.hasAttacked(false);
        minion.attack(PlayerSide.WAITING_PLAYER, 2, board, false);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 2);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(2).getHealth(), 7);
        assertEquals(currentPlayer.getCharacter(3).getHealth(), 5);
        assertEquals(waitingPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getHealth(), 7 - 5);

        assertEquals(currentPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(2).getTotalAttack(), 7);
        assertEquals(currentPlayer.getCharacter(3).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getTotalAttack(), 7);

        assertTrue(currentPlayer.getCharacter(3) instanceof BaineBloodhoof);
    }

    @Test
    public void test3() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 2);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(2).getHealth(), 5);
        assertEquals(currentPlayer.getCharacter(3).getHealth(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getHealth(), 7);

        assertEquals(currentPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(2).getTotalAttack(), 5);
        assertEquals(currentPlayer.getCharacter(3).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getTotalAttack(), 7);

        //----------------------------------------------------------

        Minion minion = currentPlayer.getCharacter(2);
        assertTrue(minion instanceof CairneBloodhoof);

        minion.hasAttacked(false);
        minion.attack(PlayerSide.WAITING_PLAYER, 2, board, false);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 2);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(2).getHealth(), 5);
        assertEquals(currentPlayer.getCharacter(3).getHealth(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getHealth(), 7 - 5);

        assertEquals(currentPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(2).getTotalAttack(), 5);
        assertEquals(currentPlayer.getCharacter(3).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getTotalAttack(), 7);

        assertTrue(currentPlayer.getCharacter(2) instanceof BaineBloodhoof);
    }

    @Test
    public void test4() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 2);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(2).getHealth(), 5);
        assertEquals(currentPlayer.getCharacter(3).getHealth(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getHealth(), 7);

        assertEquals(currentPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(2).getTotalAttack(), 5);
        assertEquals(currentPlayer.getCharacter(3).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getTotalAttack(), 7);

        //----------------------------------------------------------
        HearthTreeNode fb = new HearthTreeNode(board.data_.flipPlayers());
        currentPlayer = fb.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        waitingPlayer = fb.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        Minion minion = currentPlayer.getCharacter(2);

        minion.hasAttacked(false);
        minion.attack(PlayerSide.WAITING_PLAYER, 2, fb, false);

        assertEquals(fb.data_.getCurrentPlayer().getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 3);
        assertEquals(fb.data_.getCurrentPlayer().getMana(), 8);
        assertEquals(fb.data_.getWaitingPlayer().getMana(), 2);
        assertEquals(fb.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(fb.data_.getWaitingPlayer().getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(2).getHealth(), 7 - 5);
        assertEquals(waitingPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getHealth(), 5);
        assertEquals(waitingPlayer.getCharacter(3).getHealth(), 7);

        assertEquals(currentPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(2).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getCharacter(3).getTotalAttack(), 7);

        assertTrue(waitingPlayer.getCharacter(2) instanceof BaineBloodhoof);
    }

    @Test
    public void test5() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board);

        ret = new BloodfenRaptor().summonMinion(PlayerSide.CURRENT_PLAYER, currentPlayer.getHero(), ret, false, true);
        ret = new BloodfenRaptor().summonMinion(PlayerSide.CURRENT_PLAYER, currentPlayer.getHero(), ret, false, true);
        ret = new BloodfenRaptor().summonMinion(PlayerSide.CURRENT_PLAYER, currentPlayer.getHero(), ret, false, true);
        ret = new BloodfenRaptor().summonMinion(PlayerSide.CURRENT_PLAYER, currentPlayer.getHero(), ret, false, true);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 7);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 2);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(2).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(3).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(4).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(5).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(6).getHealth(), 5);
        assertEquals(currentPlayer.getCharacter(7).getHealth(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getHealth(), 7);

        assertEquals(currentPlayer.getCharacter(1).getTotalAttack(), 4);
        assertEquals(currentPlayer.getCharacter(2).getTotalAttack(), 4);
        assertEquals(currentPlayer.getCharacter(3).getTotalAttack(), 4);
        assertEquals(currentPlayer.getCharacter(4).getTotalAttack(), 4);
        assertEquals(currentPlayer.getCharacter(5).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(6).getTotalAttack(), 5);
        assertEquals(currentPlayer.getCharacter(7).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getTotalAttack(), 7);

        //----------------------------------------------------------
        HearthTreeNode fb = new HearthTreeNode(board.data_.flipPlayers());
        currentPlayer = fb.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        waitingPlayer = fb.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        Minion minion = currentPlayer.getCharacter(2);

        minion.hasAttacked(false);
        minion.attack(PlayerSide.WAITING_PLAYER, 6, fb, false);

        assertEquals(fb.data_.getCurrentPlayer().getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 7);
        assertEquals(fb.data_.getCurrentPlayer().getMana(), 8);
        assertEquals(fb.data_.getWaitingPlayer().getMana(), 2);
        assertEquals(fb.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(fb.data_.getWaitingPlayer().getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(2).getHealth(), 7 - 5);
        assertEquals(waitingPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(3).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(4).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(5).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(6).getHealth(), 5);
        assertEquals(waitingPlayer.getCharacter(7).getHealth(), 7);

        assertEquals(currentPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(2).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getCharacter(2).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getCharacter(3).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getCharacter(4).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getCharacter(5).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(6).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getCharacter(7).getTotalAttack(), 7);

        assertTrue(waitingPlayer.getCharacter(6) instanceof BaineBloodhoof);
    }

    @Test
    public void testKillCairneWithFireball() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 2);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(2).getHealth(), 5);
        assertEquals(currentPlayer.getCharacter(3).getHealth(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getHealth(), 7);

        assertEquals(currentPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(2).getTotalAttack(), 5);
        assertEquals(currentPlayer.getCharacter(3).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getTotalAttack(), 7);

        //----------------------------------------------------------
        HearthTreeNode fb = new HearthTreeNode(board.data_.flipPlayers());
        currentPlayer = fb.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        waitingPlayer = fb.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        Minion minion = currentPlayer.getCharacter(2);

        fb.data_.getCurrentPlayer().placeCardHand(new Fireball());
        Card fireball = fb.data_.getCurrentPlayer().getHand().get(0);

        minion.hasAttacked(false);
        fireball.useOn(PlayerSide.WAITING_PLAYER, 2, fb);

        assertEquals(fb.data_.getCurrentPlayer().getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 3);
        assertEquals(fb.data_.getCurrentPlayer().getMana(), 4);
        assertEquals(fb.data_.getWaitingPlayer().getMana(), 2);
        assertEquals(fb.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(fb.data_.getWaitingPlayer().getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(2).getHealth(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getHealth(), 5);
        assertEquals(waitingPlayer.getCharacter(3).getHealth(), 7);

        assertEquals(currentPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(2).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getCharacter(3).getTotalAttack(), 7);

        assertTrue(waitingPlayer.getCharacter(2) instanceof BaineBloodhoof);
    }
}
