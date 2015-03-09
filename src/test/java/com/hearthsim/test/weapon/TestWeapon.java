package com.hearthsim.test.weapon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.hearthsim.model.PlayerModel;
import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.card.weapon.concrete.AssassinsBlade;
import com.hearthsim.card.weapon.concrete.FieryWarAxe;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestWeapon {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    private static final byte mana = 2;
    private static final byte attack0 = 2;
    private static final byte health0 = 3;
    private static final byte health1 = 7;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        Minion minion0_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion0_1 = new Minion("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);
        Minion minion1_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion1_1 = new Minion("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_1);

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_1);

        FieryWarAxe fb = new FieryWarAxe();
        currentPlayer.placeCardHand(fb);

        currentPlayer.setMana((byte) 2);
    }

    @Test
    public void testEquip() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);

        assertEquals(board, ret);
        assertEquals(currentPlayer.getHand().size(), 0);
        assertTrue(theCard.hasBeenUsed());
        //assertFalse(theCard.isInHand()); TODO existing bug

        assertEquals(currentPlayer.getHero().getWeapon().getWeaponCharge(), 2);
        assertEquals(currentPlayer.getHero().getTotalAttack(), 3);
    }

    @Test
    public void testHeroCanAttack() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);

        Minion hero = currentPlayer.getHero();
        Minion target = waitingPlayer.getCharacter(0);
        ret = hero.attack(PlayerSide.WAITING_PLAYER, target, ret, null, null, false);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 27);

        assertEquals(currentPlayer.getHero().getTotalAttack(), 3);
        assertEquals(currentPlayer.getHero().getWeapon().getWeaponCharge(), 1);
    }

    @Test
    public void testHeroCannotAttackTwice() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);

        Minion hero = currentPlayer.getHero();
        Minion target = waitingPlayer.getCharacter(0);
        ret = hero.attack(PlayerSide.WAITING_PLAYER, target, ret, null, null, false);

        hero = currentPlayer.getHero();
        target = waitingPlayer.getCharacter(0);
        ret = hero.attack(PlayerSide.WAITING_PLAYER, target, ret, null, null, false);
        assertNull(ret);

        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 27);

        assertEquals(currentPlayer.getHero().getTotalAttack(), 3);
        assertEquals(currentPlayer.getHero().getWeapon().getWeaponCharge(), 1);
    }

    @Test
    public void testWeaponBreaks() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
        currentPlayer.getHero().getWeapon().setWeaponCharge((byte) 1);

        Minion hero = currentPlayer.getHero();
        Minion target = waitingPlayer.getCharacter(0);
        ret = hero.attack(PlayerSide.WAITING_PLAYER, target, ret, null, null, false);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 27);

        assertEquals(currentPlayer.getHero().getTotalAttack(), 0);
        assertNull(currentPlayer.getHero().getWeapon());
    }

    @Test
    public void testDestroysEquippedWeapon() throws HSException {
        AssassinsBlade otherWeapon = new AssassinsBlade();
        currentPlayer.placeCardHand(otherWeapon);
        currentPlayer.setMana((byte) 7);
        currentPlayer.getHand().get(1).useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertTrue(theCard.hasBeenUsed());
        //assertFalse(theCard.isInHand()); TODO existing bug

        assertEquals(currentPlayer.getHero().getWeapon().getWeaponCharge(), 2);
        assertEquals(currentPlayer.getHero().getTotalAttack(), 3);
    }

    @Test
    public void testCannotTargetMinion() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board, null, null);
        assertNull(ret);

        assertFalse(theCard.hasBeenUsed());
        assertEquals(currentPlayer.getHand().size(), 1);

        assertNull(currentPlayer.getHero().getWeapon());
        assertEquals(currentPlayer.getHero().getTotalAttack(), 0);

        assertEquals(currentPlayer.getMinions().get(0).getAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 2);
    }

    @Test
    public void testCannotTargetOpponent() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 0, board, null, null);
        assertNull(ret);

        assertFalse(theCard.hasBeenUsed());
        assertEquals(currentPlayer.getHand().size(), 1);

        assertNull(currentPlayer.getHero().getWeapon());
        assertEquals(currentPlayer.getHero().getTotalAttack(), 0);

        assertNull(waitingPlayer.getHero().getWeapon());
        assertEquals(waitingPlayer.getHero().getTotalAttack(), 0);
    }
}
