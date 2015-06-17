package com.hearthsim.test;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.KoboldGeomancer;
import com.hearthsim.card.basic.spell.HolySmite;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionMock;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestSpellDamage {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    private static final byte mana = 2;
    private static final byte attack0 = 2;
    private static final byte health0 = 2;
    private static final byte health1 = 3;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        Minion minion0_0 = new MinionMock("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion0_1 = new MinionMock("" + 0, mana, attack0, health1, attack0, health1, health1);
        Minion minion1_0 = new MinionMock("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion1_1 = new MinionMock("" + 0, mana, attack0, health1, attack0, health1, health1);

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_1);

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_1);

        currentPlayer.setMana((byte) 2);
    }

    @Test
    public void testCanTargetOwnHero() throws HSException {
        HolySmite hs = new HolySmite();
        currentPlayer.placeCardHand(hs);

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(ret, board);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getMana(), 1);

        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 2);

        assertEquals(currentPlayer.getHero().getHealth(), 28);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health1);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health1);
    }

    @Test
    public void testCanTargetEnemyHero() throws HSException {
        HolySmite hs = new HolySmite();
        currentPlayer.placeCardHand(hs);

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);
        assertEquals(ret, board);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getMana(), 1);

        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 2);

        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 28);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health1);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health1);
    }

    @Test
    public void testKillOwnMinion() throws HSException {
        HolySmite hs = new HolySmite();
        currentPlayer.placeCardHand(hs);

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1, board);
        assertEquals(ret, board);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getMana(), 1);

        assertEquals(currentPlayer.getNumMinions(), 1);
        assertEquals(waitingPlayer.getNumMinions(), 2);

        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health1);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health1);
    }

    @Test
    public void testCanTargetOwnMinion() throws HSException {
        HolySmite hs = new HolySmite();
        currentPlayer.placeCardHand(hs);

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_2, board);
        assertEquals(ret, board);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getMana(), 1);

        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 2);

        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health1 - 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health1);
    }

    @Test
    public void testKillEnemyMinion() throws HSException {
        HolySmite hs = new HolySmite();
        currentPlayer.placeCardHand(hs);

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_1, board);
        assertEquals(ret, board);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getMana(), 1);

        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 1);

        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health1);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health1);
    }

    @Test
    public void testTargetEnemyMinion() throws HSException {
        HolySmite hs = new HolySmite();
        currentPlayer.placeCardHand(hs);

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_2, board);
        assertEquals(ret, board);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getMana(), 1);

        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 2);

        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health1);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health1 - 2);
    }

    @Test
    public void testSpellpower() throws HSException {
        KoboldGeomancer kobold = new KoboldGeomancer();
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, kobold);

        HolySmite hs = new HolySmite();
        currentPlayer.placeCardHand(hs);

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_2, board);
        assertEquals(ret, board);

        assertEquals(waitingPlayer.getNumMinions(), 1);
    }

    @Test
    public void testDeepCopyDamage() throws HSException {
        HolySmite smite = new HolySmite();
        HolySmite copy = (HolySmite)smite.deepCopy();

        assertEquals(smite.getAttack(), copy.getAttack());
    }
}
