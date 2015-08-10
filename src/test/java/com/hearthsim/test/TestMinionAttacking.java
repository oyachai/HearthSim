package com.hearthsim.test;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.*;
import com.hearthsim.card.classic.minion.common.FaerieDragon;
import com.hearthsim.card.classic.minion.common.StranglethornTiger;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestMinionAttacking {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    private BloodfenRaptor raptor;
    private ChillwindYeti yeti;
    private RiverCrocolisk croc;

    @Before
    public void setUp() throws Exception {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        raptor = new BloodfenRaptor();
        yeti = new ChillwindYeti();
        croc = new RiverCrocolisk();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, raptor);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, yeti);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, croc);

        currentPlayer.setMana((byte) 8);
        waitingPlayer.setMana((byte) 8);
    }

    @Test
    public void testCharge() throws HSException {
        BluegillWarrior murloc = new BluegillWarrior();
        currentPlayer.placeCardHand(murloc);

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        Minion theAttacker = currentPlayer.getCharacter(CharacterIndex.MINION_1);
        assertTrue(theAttacker.canAttack());

        ret = theAttacker.attack(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        assertEquals(30, currentPlayer.getHero().getHealth());
        assertEquals(28, waitingPlayer.getHero().getHealth());
    }

    @Test
    public void testSummoningSickness() throws HSException {
        MurlocRaider murloc = new MurlocRaider();
        currentPlayer.placeCardHand(murloc);

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        Minion theAttacker = currentPlayer.getCharacter(CharacterIndex.MINION_1);
        assertFalse(theAttacker.canAttack());

        ret = theAttacker.attack(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);
        assertNull(ret);

        assertEquals(30, waitingPlayer.getHero().getHealth());
    }

    @Test
    public void testAttackEnemyHero() throws HSException {
        HearthTreeNode ret = raptor.attack(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        assertFalse(raptor.canAttack());
        assertEquals(30, currentPlayer.getHero().getHealth());
        assertEquals(27, waitingPlayer.getHero().getHealth());
    }

    @Test
    public void testAttackEnemyMinion() throws HSException {
        HearthTreeNode ret = raptor.attack(PlayerSide.WAITING_PLAYER, croc, board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getNumMinions(), 1);
        assertEquals(waitingPlayer.getNumMinions(), 0);
    }

    @Test
    public void testAttackFaerieMinion() throws HSException {
        FaerieDragon faerie = new FaerieDragon();
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, faerie);

        HearthTreeNode ret = raptor.attack(PlayerSide.WAITING_PLAYER, faerie, board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getNumMinions(), 1);
        assertEquals(waitingPlayer.getNumMinions(), 1);
    }

    @Test
    public void testAttackStealthedMinion() throws HSException {
        StranglethornTiger tiger = new StranglethornTiger();
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, tiger);

        HearthTreeNode ret = raptor.attack(PlayerSide.WAITING_PLAYER, tiger, board);
        assertNull(ret);

        assertEquals(2, currentPlayer.getNumMinions());
        assertEquals(2, waitingPlayer.getNumMinions());
    }

    @Test
    public void testAttackBreaksStealth() throws HSException {
        raptor.setStealthedUntilRevealed(true);
        HearthTreeNode ret = raptor.attack(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        assertFalse(raptor.isStealthed());
        assertEquals(30, currentPlayer.getHero().getHealth());
        assertEquals(27, waitingPlayer.getHero().getHealth());
    }

    @Test
    public void testAttackOwnHero() throws HSException {
        HearthTreeNode ret = raptor.attack(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertNull(ret);

        assertEquals(30, currentPlayer.getHero().getHealth());
        assertEquals(30, waitingPlayer.getHero().getHealth());
    }

    @Test
    public void testAttackOwnMinion() throws HSException {
        HearthTreeNode ret = raptor.attack(PlayerSide.CURRENT_PLAYER, yeti, board);
        assertNull(ret);
        assertEquals(currentPlayer.getNumMinions(), 2);
    }

    @Test
    public void testWindfury() throws HSException {
        raptor.setWindfury(true);

        HearthTreeNode ret = raptor.attack(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        assertTrue(raptor.hasWindFuryAttacked());
        assertTrue(raptor.canAttack());
        assertEquals(30, currentPlayer.getHero().getHealth());
        assertEquals(27, waitingPlayer.getHero().getHealth());

        ret = raptor.attack(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        assertTrue(raptor.hasWindFuryAttacked());
        assertFalse(raptor.canAttack());
        assertEquals(30, currentPlayer.getHero().getHealth());
        assertEquals(24, waitingPlayer.getHero().getHealth());
    }

    @Test
    public void testWindfuryPostAttack() throws HSException {
        HearthTreeNode ret = raptor.attack(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        assertFalse(raptor.hasWindFuryAttacked());
        assertFalse(raptor.canAttack());
        assertEquals(30, currentPlayer.getHero().getHealth());
        assertEquals(27, waitingPlayer.getHero().getHealth());

        raptor.setWindfury(true);
        assertTrue(raptor.hasWindFuryAttacked());
        assertTrue(raptor.canAttack());

        ret = raptor.attack(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        assertTrue(raptor.hasWindFuryAttacked());
        assertFalse(raptor.canAttack());
        assertEquals(30, currentPlayer.getHero().getHealth());
        assertEquals(24, waitingPlayer.getHero().getHealth());
    }

    @Test
    public void testExtraAttackDamage() throws HSException {
        raptor.setExtraAttackUntilTurnEnd((byte)2);

        HearthTreeNode ret = raptor.attack(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        assertFalse(raptor.canAttack());
        assertEquals(30, currentPlayer.getHero().getHealth());
        assertEquals(25, waitingPlayer.getHero().getHealth());

        assertEquals(2, raptor.getExtraAttackUntilTurnEnd());
    }
}
