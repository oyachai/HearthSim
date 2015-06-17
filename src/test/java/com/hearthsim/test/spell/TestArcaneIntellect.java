package com.hearthsim.test.spell;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.Deck;
import com.hearthsim.card.basic.minion.BloodfenRaptor;
import com.hearthsim.card.basic.spell.ArcaneIntellect;
import com.hearthsim.card.basic.spell.TheCoin;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionMock;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.util.HearthActionBoardPair;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TestArcaneIntellect {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    private static final byte mana = 2;
    private static final byte attack0 = 2;
    private static final byte health0 = 5;
    private static final byte health1 = 1;

    @Before
    public void setup() throws HSException {

        int numCards = 10;
        Card cards[] = new Card[numCards];
        for (int index = 0; index < numCards; ++index) {
            cards[index] = new BloodfenRaptor();
        }

        Deck deck = new Deck(cards);
        PlayerModel playerModel0 = new PlayerModel((byte)0, "player0", new TestHero(), deck);
        PlayerModel playerModel1 = new PlayerModel((byte)1, "player1", new TestHero(), deck);

        board = new HearthTreeNode(new BoardModel(playerModel0, playerModel1));
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        Minion minion0 = new MinionMock("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion1 = new MinionMock("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion2 = new MinionMock("" + 0, mana, attack0, health1, attack0, health1, health1);
        Minion minion3 = new MinionMock("" + 0, mana, attack0, health0, attack0, health0, health0);

        ArcaneIntellect fb = new ArcaneIntellect();
        currentPlayer.placeCardHand(fb);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion2);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion3);

        currentPlayer.setMana((byte) 5);
    }

    @Test
    public void testWithFullDeck() throws HSException {

        Card cards[] = new Card[10];
        for (int index = 0; index < 10; ++index) {
            cards[index] = new TheCoin();
        }

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode res;

        res = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertNotNull(res);
        assertEquals(currentPlayer.getHand().size(), 0);
        assertTrue(res instanceof CardDrawNode);
        assertEquals(((CardDrawNode)res).getNumCardsToDraw(), 2);

        assertEquals(currentPlayer.getNumMinions(), 1);
        assertEquals(waitingPlayer.getNumMinions(), 3);
        assertEquals(currentPlayer.getMana(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health1);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_3).getHealth(), health0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), attack0);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
    }

    @Test
    public void testWithNearlyEmptyDeck() throws HSException {

        Card cards[] = new Card[1];
        for (int index = 0; index < 1; ++index) {
            cards[index] = new TheCoin();
        }

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode res;

        res = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertNotNull(res);
        assertEquals(currentPlayer.getHand().size(), 0);
        assertTrue(res instanceof CardDrawNode);
        assertEquals(((CardDrawNode)res).getNumCardsToDraw(), 2);

        assertEquals(currentPlayer.getNumMinions(), 1);
        assertEquals(waitingPlayer.getNumMinions(), 3);
        assertEquals(currentPlayer.getMana(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health1);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_3).getHealth(), health0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), attack0);
    }

    @Test
    public void test2() throws HSException {
        currentPlayer.setMana((byte) 3);
        waitingPlayer.setMana((byte) 3);

        currentPlayer.setMaxMana((byte) 3);
        waitingPlayer.setMaxMana((byte) 3);

        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
        List<HearthActionBoardPair> ab = ai0.playTurn(0, board.data_);
        BoardModel resBoard = ab.get(ab.size() - 1).board;

        assertFalse(resBoard == null);

        assertEquals(resBoard.getCurrentPlayer().getMana(), 0);
        assertEquals(resBoard.getWaitingPlayer().getMana(), 3);
        assertEquals(resBoard.getCurrentPlayer().getHand().size(), 2);
        assertEquals(resBoard.modelForSide(PlayerSide.CURRENT_PLAYER).getNumMinions(), 1);
        assertEquals(resBoard.modelForSide(PlayerSide.WAITING_PLAYER).getNumMinions(), 2);
    }

    @Test
    public void testCanPlayDrawnCard() throws HSException {
        currentPlayer.setMana((byte) 6);
        waitingPlayer.setMana((byte) 6);

        currentPlayer.setMaxMana((byte) 6);
        waitingPlayer.setMaxMana((byte)6);

        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
        List<HearthActionBoardPair> ab = ai0.playTurn(0, board.data_);
        BoardModel resBoard = ab.get(ab.size() - 1).board;

        assertFalse(resBoard == null);

        assertEquals(resBoard.getCurrentPlayer().getMana(), 1);
        assertEquals(resBoard.getWaitingPlayer().getMana(), 6);
        assertEquals(resBoard.getCurrentPlayer().getHand().size(), 1);
        assertEquals(resBoard.modelForSide(PlayerSide.CURRENT_PLAYER).getNumMinions(), 2);
        assertEquals(resBoard.modelForSide(PlayerSide.WAITING_PLAYER).getNumMinions(), 2);
    }

    @Test
    public void test4() throws HSException {
        currentPlayer.setMana((byte) 9);
        waitingPlayer.setMana((byte) 9);

        currentPlayer.setMaxMana((byte) 9);
        waitingPlayer.setMaxMana((byte)9);

        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
        List<HearthActionBoardPair> ab = ai0.playTurn(0, board.data_);
        BoardModel resBoard = ab.get(ab.size() - 1).board;

        assertFalse(resBoard == null);

        assertEquals(resBoard.getCurrentPlayer().getMana(), 2);
        assertEquals(resBoard.getWaitingPlayer().getMana(), 9);
        assertEquals(resBoard.getCurrentPlayer().getHand().size(), 0);
        assertEquals(resBoard.modelForSide(PlayerSide.CURRENT_PLAYER).getNumMinions(), 3);
        assertEquals(resBoard.modelForSide(PlayerSide.WAITING_PLAYER).getNumMinions(), 2);
    }
}
