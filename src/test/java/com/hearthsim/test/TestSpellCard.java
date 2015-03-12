package com.hearthsim.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.hearthsim.model.PlayerModel;
import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.FaerieDragon;
import com.hearthsim.card.minion.concrete.RiverCrocolisk;
import com.hearthsim.card.spellcard.concrete.HolySmite;
import com.hearthsim.card.spellcard.concrete.RockbiterWeapon;
import com.hearthsim.card.spellcard.concrete.Sap;
import com.hearthsim.card.spellcard.concrete.ShadowBolt;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestSpellCard {
    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        Minion minion0_0 = new BloodfenRaptor();
        Minion minion1_0 = new RiverCrocolisk();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);

        currentPlayer.setMana((byte) 10);
        currentPlayer.setMaxMana((byte) 10);
    }

    @Test
    public void testTargetOwnHero() throws HSException {
        currentPlayer.placeCardHand(new HolySmite());

        Minion target = currentPlayer.getCharacter(0);
        Card theCard = currentPlayer.getHand().get(0);
        assertTrue(theCard.canBeUsedOn(PlayerSide.CURRENT_PLAYER, target, board.data_));
    }

    @Test
    public void testTargetOwnHeroFailure() throws HSException {
        currentPlayer.placeCardHand(new ShadowBolt());

        Minion target = currentPlayer.getCharacter(0);
        Card theCard = currentPlayer.getHand().get(0);
        assertFalse(theCard.canBeUsedOn(PlayerSide.CURRENT_PLAYER, target, board.data_));
    }

    @Test
    public void testTargetOwnMinion() throws HSException {
        currentPlayer.placeCardHand(new RockbiterWeapon());

        Minion target = currentPlayer.getCharacter(1);
        Card theCard = currentPlayer.getHand().get(0);
        assertTrue(theCard.canBeUsedOn(PlayerSide.CURRENT_PLAYER, target, board.data_));
    }

    @Test
    public void testTargetOwnMinionFailure() throws HSException {
        currentPlayer.placeCardHand(new Sap());

        Minion target = currentPlayer.getCharacter(1);
        Card theCard = currentPlayer.getHand().get(0);
        assertFalse(theCard.canBeUsedOn(PlayerSide.CURRENT_PLAYER, target, board.data_));
    }

    @Test
    public void testTargetEnemyHero() throws HSException {
        currentPlayer.placeCardHand(new HolySmite());

        Minion target = waitingPlayer.getCharacter(0);
        Card theCard = currentPlayer.getHand().get(0);
        assertTrue(theCard.canBeUsedOn(PlayerSide.WAITING_PLAYER, target, board.data_));
    }

    @Test
    public void testTargetEnemyHeroFailure() throws HSException {
        currentPlayer.placeCardHand(new ShadowBolt());

        Minion target = waitingPlayer.getCharacter(0);
        Card theCard = currentPlayer.getHand().get(0);
        assertFalse(theCard.canBeUsedOn(PlayerSide.WAITING_PLAYER, target, board.data_));
    }

    @Test
    public void testTargetEnemyMinion() throws HSException {
        currentPlayer.placeCardHand(new Sap());

        Minion target = waitingPlayer.getCharacter(1);
        Card theCard = currentPlayer.getHand().get(0);
        assertTrue(theCard.canBeUsedOn(PlayerSide.WAITING_PLAYER, target, board.data_));
    }

    @Test
    public void testTargetEnemyMinionFailure() throws HSException {
        currentPlayer.placeCardHand(new RockbiterWeapon());

        Minion target = waitingPlayer.getCharacter(1);
        Card theCard = currentPlayer.getHand().get(0);
        assertFalse(theCard.canBeUsedOn(PlayerSide.WAITING_PLAYER, target, board.data_));
    }

    @Test
    public void testTargetStealthedMinion() throws HSException {
        currentPlayer.placeCardHand(new HolySmite());

        Minion target = waitingPlayer.getCharacter(1);
        target.setStealthed(true);
        Card theCard = currentPlayer.getHand().get(0);
        assertFalse(theCard.canBeUsedOn(PlayerSide.WAITING_PLAYER, target, board.data_));
    }

    @Test
    public void testTargetFaerieMinion() throws HSException {
        currentPlayer.placeCardHand(new HolySmite());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new FaerieDragon());

        Minion target = waitingPlayer.getCharacter(2);
        Card theCard = currentPlayer.getHand().get(0);
        assertFalse(theCard.canBeUsedOn(PlayerSide.WAITING_PLAYER, target, board.data_));
    }

    @Test
    public void testDeepCopyOnlyTargetsEnemyMinion() throws HSException {
        Sap sap = new Sap();
        Card copy = sap.deepCopy();

        Minion ownMinion = currentPlayer.getCharacter(1);
        Minion ownHero = currentPlayer.getCharacter(0);
        Minion enemyMinion = waitingPlayer.getCharacter(1);
        Minion enemyHero = waitingPlayer.getCharacter(0);

        assertFalse(copy.canBeUsedOn(PlayerSide.CURRENT_PLAYER, ownMinion, board.data_));
        assertFalse(copy.canBeUsedOn(PlayerSide.CURRENT_PLAYER, ownHero, board.data_));
        assertTrue(copy.canBeUsedOn(PlayerSide.WAITING_PLAYER, enemyMinion, board.data_));
        assertFalse(copy.canBeUsedOn(PlayerSide.WAITING_PLAYER, enemyHero, board.data_));
    }

    @Test
    public void testDeepCopyCannotTargetEnemyMinion() throws HSException {
        RockbiterWeapon rockbiter = new RockbiterWeapon();
        Card copy = rockbiter.deepCopy();

        Minion ownMinion = currentPlayer.getCharacter(1);
        Minion ownHero = currentPlayer.getCharacter(0);
        Minion enemyMinion = waitingPlayer.getCharacter(1);
        Minion enemyHero = waitingPlayer.getCharacter(0);

        assertTrue(copy.canBeUsedOn(PlayerSide.CURRENT_PLAYER, ownMinion, board.data_));
        assertTrue(copy.canBeUsedOn(PlayerSide.CURRENT_PLAYER, ownHero, board.data_));
        assertFalse(copy.canBeUsedOn(PlayerSide.WAITING_PLAYER, enemyMinion, board.data_));
        assertFalse(copy.canBeUsedOn(PlayerSide.WAITING_PLAYER, enemyHero, board.data_));
    }
}
