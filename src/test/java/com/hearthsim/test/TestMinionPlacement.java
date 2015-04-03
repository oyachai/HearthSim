package com.hearthsim.test;

import com.hearthsim.card.minion.concrete.*;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestMinionPlacement {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    private BloodfenRaptor raptor;
    private ChillwindYeti yeti;

    @Before
    public void setUp() throws Exception {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        raptor = new BloodfenRaptor();
        yeti = new ChillwindYeti();
        RiverCrocolisk croc = new RiverCrocolisk();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, raptor);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, yeti);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, croc);

        currentPlayer.setMana((byte) 8);
        waitingPlayer.setMana((byte) 8);

        currentPlayer.setMaxMana((byte) 8);
        waitingPlayer.setMaxMana((byte) 8);
    }

    @Test
    public void testCanBeUsedOnOpponentHero() throws HSException {
        Archmage archmage = new Archmage();
        currentPlayer.placeCardHand(archmage);

        HearthTreeNode ret = archmage.useOn(PlayerSide.WAITING_PLAYER, 0, this.board);
        assertNull(ret);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 1);
    }

    @Test
    public void testCanBeUsedOnOpponentMinion() throws HSException {
        Archmage archmage = new Archmage();
        currentPlayer.placeCardHand(archmage);

        HearthTreeNode ret = archmage.useOn(PlayerSide.WAITING_PLAYER, 1, this.board);
        assertNull(ret);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 1);
    }

    @Test
    public void testCanBeUsedOnOwnHero() throws HSException {
        Archmage archmage = new Archmage();
        currentPlayer.placeCardHand(archmage);

        HearthTreeNode ret = archmage.useOn(PlayerSide.CURRENT_PLAYER, 0, this.board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getMana(), 2);
        assertEquals(waitingPlayer.getMana(), 8);

        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(archmage, this.currentPlayer.getCharacter(1));
        assertEquals(raptor, this.currentPlayer.getCharacter(2));
        assertEquals(yeti, this.currentPlayer.getCharacter(3));

        assertEquals(waitingPlayer.getNumMinions(), 1);
    }

    @Test
    public void testCanBeUsedOnOwnMinionMiddle() throws HSException {
        Archmage archmage = new Archmage();
        currentPlayer.placeCardHand(archmage);

        HearthTreeNode ret = archmage.useOn(PlayerSide.CURRENT_PLAYER, 1, this.board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getMana(), 2);
        assertEquals(waitingPlayer.getMana(), 8);

        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(raptor, this.currentPlayer.getCharacter(1));
        assertEquals(archmage, this.currentPlayer.getCharacter(2));
        assertEquals(yeti, this.currentPlayer.getCharacter(3));

        assertEquals(waitingPlayer.getNumMinions(), 1);
    }

    @Test
    public void testCanBeUsedOnOwnMinionRight() throws HSException {
        Archmage archmage = new Archmage();
        currentPlayer.placeCardHand(archmage);

        HearthTreeNode ret = archmage.useOn(PlayerSide.CURRENT_PLAYER, 2, this.board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getMana(), 2);
        assertEquals(waitingPlayer.getMana(), 8);

        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(raptor, this.currentPlayer.getCharacter(1));
        assertEquals(yeti, this.currentPlayer.getCharacter(2));
        assertEquals(archmage, this.currentPlayer.getCharacter(3));

        assertEquals(waitingPlayer.getNumMinions(), 1);
    }

    @Test
    public void testCanBeUsedOnFullBoard() throws HSException {
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new SilverHandRecruit());

        Archmage archmage = new Archmage();
        currentPlayer.placeCardHand(archmage);

        HearthTreeNode ret = archmage.useOn(PlayerSide.CURRENT_PLAYER, 1, this.board);
        assertNull(ret);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getNumMinions(), 7);
        assertEquals(waitingPlayer.getNumMinions(), 1);
    }

    @Test
    public void testAddsSpellDamage() throws HSException {
        Archmage archmage = new Archmage();
        currentPlayer.placeCardHand(archmage);

        HearthTreeNode ret = archmage.useOn(PlayerSide.CURRENT_PLAYER, 1, this.board);
        assertEquals(board, ret);

        assertEquals(1, currentPlayer.getSpellDamage());
    }

    @Test
    public void testSpellDamageStacks() throws HSException {
        KoboldGeomancer kobold = new KoboldGeomancer();
        currentPlayer.placeCardHand(kobold);

        HearthTreeNode ret = kobold.useOn(PlayerSide.CURRENT_PLAYER, 1, this.board);
        assertEquals(board, ret);

        kobold = new KoboldGeomancer();
        currentPlayer.placeCardHand(kobold);

        ret = kobold.useOn(PlayerSide.CURRENT_PLAYER, 1, this.board);
        assertEquals(board, ret);

        assertEquals(2, currentPlayer.getSpellDamage());
    }
}
