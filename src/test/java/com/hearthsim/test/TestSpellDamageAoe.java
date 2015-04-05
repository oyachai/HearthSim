package com.hearthsim.test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.KoboldGeomancer;
import com.hearthsim.card.spellcard.concrete.ArcaneExplosion;
import com.hearthsim.card.spellcard.concrete.Consecration;
import com.hearthsim.card.spellcard.concrete.Hellfire;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestSpellDamageAoe {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    private static final byte mana = 2;
    private static final byte attack0 = 2;
    private static final byte health0 = 5;
    private static final byte health1 = 1;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        Minion minion0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion1 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion2 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1);
        Minion minion3 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);

        ArcaneExplosion fb = new ArcaneExplosion();
        currentPlayer.placeCardHand(fb);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion2);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion3);

        currentPlayer.setMana((byte) 3);
    }

    @Test
    public void testHitsEnemyMinions() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getMana(), 1);
        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);

        assertEquals(currentPlayer.getNumMinions(), 1);
        assertEquals(waitingPlayer.getNumMinions(), 2);

        assertEquals(currentPlayer.getMinions().get(0).getHealth(), health0);
        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), attack0);

        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), health0 - 1);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), health0 - 1);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), attack0);
    }

    @Test
    public void testHitsEnemyCharacters() throws HSException {
        Consecration consecration = new Consecration();
        currentPlayer.placeCardHand(consecration);
        currentPlayer.setMana((byte) 5);

        Card theCard = currentPlayer.getHand().get(1);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getMana(), 1);
        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 28);

        assertEquals(currentPlayer.getNumMinions(), 1);
        assertEquals(waitingPlayer.getNumMinions(), 2);

        assertEquals(currentPlayer.getMinions().get(0).getHealth(), health0);
        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), attack0);

        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), health0 - 2);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), health0 - 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), attack0);
    }

    @Test
    public void testHitsAllCharacters() throws HSException {
        Hellfire hellfire = new Hellfire();
        currentPlayer.placeCardHand(hellfire);
        currentPlayer.setMana((byte) 5);

        Card theCard = currentPlayer.getHand().get(1);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getMana(), 1);
        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getHero().getHealth(), 27);
        assertEquals(waitingPlayer.getHero().getHealth(), 27);

        assertEquals(currentPlayer.getNumMinions(), 1);
        assertEquals(waitingPlayer.getNumMinions(), 2);

        assertEquals(currentPlayer.getMinions().get(0).getHealth(), health0 - 3);
        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), attack0);

        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), health0 - 3);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), health0 - 3);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), attack0);
    }

    @Test
    public void testSpellpower() throws HSException {
        Minion kobold = new KoboldGeomancer();
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, kobold);

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getMana(), 1);
        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);

        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 2);

        assertEquals(currentPlayer.getMinions().get(0).getHealth(), health0);
        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), attack0);

        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), health0 - 2);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), health0 - 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), attack0);
    }

    @Test
    public void testCannotTargetOpponentMinion() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 1, board);
        assertNull(ret);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getMana(), 3);

        assertEquals(currentPlayer.getNumMinions(), 1);
        assertEquals(waitingPlayer.getNumMinions(), 3);

        assertEquals(currentPlayer.getMinions().get(0).getHealth(), health0);
        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), attack0);

        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), health0);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), health1);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getMinions().get(2).getHealth(), health0);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalAttack(), attack0);
    }

    @Test
    public void testCannotTargetOpponent() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 0, board);
        assertNull(ret);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getMana(), 3);

        assertEquals(currentPlayer.getNumMinions(), 1);
        assertEquals(waitingPlayer.getNumMinions(), 3);

        assertEquals(currentPlayer.getMinions().get(0).getHealth(), health0);
        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), attack0);

        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), health0);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), health1);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getMinions().get(2).getHealth(), health0);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalAttack(), attack0);
    }

    @Test
    public void testCannotTargetMinion() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board);
        assertNull(ret);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getMana(), 3);

        assertEquals(currentPlayer.getNumMinions(), 1);
        assertEquals(waitingPlayer.getNumMinions(), 3);

        assertEquals(currentPlayer.getMinions().get(0).getHealth(), health0);
        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), attack0);

        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), health0);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), health1);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getMinions().get(2).getHealth(), health0);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalAttack(), attack0);
    }

    @Test
    public void testCannotReuse() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        theCard.hasBeenUsed(true);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board);
        assertNull(ret);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getMana(), 3);
    }
}
