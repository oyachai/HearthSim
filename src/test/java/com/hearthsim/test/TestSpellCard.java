package com.hearthsim.test;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.BloodfenRaptor;
import com.hearthsim.card.basic.minion.RiverCrocolisk;
import com.hearthsim.card.basic.spell.HolySmite;
import com.hearthsim.card.basic.spell.RockbiterWeapon;
import com.hearthsim.card.basic.spell.Sap;
import com.hearthsim.card.basic.spell.ShadowBolt;
import com.hearthsim.card.classic.minion.common.FaerieDragon;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

        Card theCard = currentPlayer.getHand().get(0);
        assertTrue(theCard.canBeUsedOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board.data_));
    }

    @Test
    public void testTargetOwnHeroFailure() throws HSException {
        currentPlayer.placeCardHand(new ShadowBolt());

        Card theCard = currentPlayer.getHand().get(0);
        assertFalse(theCard.canBeUsedOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board.data_));
    }

    @Test
    public void testTargetOwnMinion() throws HSException {
        currentPlayer.placeCardHand(new RockbiterWeapon());

        Card theCard = currentPlayer.getHand().get(0);
        assertTrue(theCard.canBeUsedOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1, board.data_));
    }

    @Test
    public void testTargetOwnMinionFailure() throws HSException {
        currentPlayer.placeCardHand(new Sap());

        Card theCard = currentPlayer.getHand().get(0);
        assertFalse(theCard.canBeUsedOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1, board.data_));
    }

    @Test
    public void testTargetEnemyHero() throws HSException {
        currentPlayer.placeCardHand(new HolySmite());

        Card theCard = currentPlayer.getHand().get(0);
        assertTrue(theCard.canBeUsedOn(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board.data_));
    }

    @Test
    public void testTargetEnemyHeroFailure() throws HSException {
        currentPlayer.placeCardHand(new ShadowBolt());

        Card theCard = currentPlayer.getHand().get(0);
        assertFalse(theCard.canBeUsedOn(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board.data_));
    }

    @Test
    public void testTargetEnemyMinion() throws HSException {
        currentPlayer.placeCardHand(new Sap());

        Card theCard = currentPlayer.getHand().get(0);
        assertTrue(theCard.canBeUsedOn(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_1, board.data_));
    }

    @Test
    public void testTargetEnemyMinionFailure() throws HSException {
        currentPlayer.placeCardHand(new RockbiterWeapon());

        Card theCard = currentPlayer.getHand().get(0);
        assertFalse(theCard.canBeUsedOn(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_1, board.data_));
    }

    @Test
    public void testTargetStealthedMinion() throws HSException {
        currentPlayer.placeCardHand(new HolySmite());

        Minion target = waitingPlayer.getCharacter(CharacterIndex.MINION_1);
        target.setStealthedUntilRevealed(true);
        Card theCard = currentPlayer.getHand().get(0);
        assertFalse(theCard.canBeUsedOn(PlayerSide.WAITING_PLAYER, target, board.data_));
    }

    @Test
    public void testTargetFaerieMinion() throws HSException {
        currentPlayer.placeCardHand(new HolySmite());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new FaerieDragon());

        Card theCard = currentPlayer.getHand().get(0);
        assertFalse(theCard.canBeUsedOn(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_2, board.data_));
    }

    @Test
    public void testDeepCopyOnlyTargetsEnemyMinion() throws HSException {
        Sap sap = new Sap();
        Card copy = sap.deepCopy();

        assertFalse(copy.canBeUsedOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1, board.data_));
        assertFalse(copy.canBeUsedOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board.data_));
        assertTrue(copy.canBeUsedOn(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_1, board.data_));
        assertFalse(copy.canBeUsedOn(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board.data_));
    }

    @Test
    public void testDeepCopyCannotTargetEnemyMinion() throws HSException {
        RockbiterWeapon rockbiter = new RockbiterWeapon();
        Card copy = rockbiter.deepCopy();

        assertTrue(copy.canBeUsedOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1, board.data_));
        assertTrue(copy.canBeUsedOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board.data_));
        assertFalse(copy.canBeUsedOn(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_1, board.data_));
        assertFalse(copy.canBeUsedOn(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board.data_));
    }
}
