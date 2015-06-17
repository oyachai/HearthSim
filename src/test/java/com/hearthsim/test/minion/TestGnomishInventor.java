package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.Deck;
import com.hearthsim.card.basic.minion.BloodfenRaptor;
import com.hearthsim.card.basic.minion.GnomishInventor;
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

public class TestGnomishInventor {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    private static final byte mana = 2;
    private static final byte attack0 = 5;
    private static final byte health0 = 3;
    private static final byte health1 = 7;

    @Before
    public void setup() throws HSException {
        Card cards[] = new Card[10];
        for (int index = 0; index < 10; ++index) {
            cards[index] = new BloodfenRaptor();
        }

        Deck deck = new Deck(cards);
        PlayerModel playerModel0 = new PlayerModel((byte)0, "player0", new TestHero(), deck);
        PlayerModel playerModel1 = new PlayerModel((byte)1, "player1", new TestHero(), deck);

        board = new HearthTreeNode(new BoardModel(playerModel0, playerModel1));
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        Minion minion0_0 = new MinionMock("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion0_1 = new MinionMock("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);
        Minion minion1_0 = new MinionMock("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion1_1 = new MinionMock("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_1);

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_1);

        Minion fb = new GnomishInventor();
        currentPlayer.placeCardHand(fb);

        currentPlayer.setMana((byte) 7);
        waitingPlayer.setMana((byte) 7);
    }

    @Test
    public void test0() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);

        assertNull(ret);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health1 - 1);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health1 - 1);
    }

    @Test
    public void test2() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1, board);

        assertFalse(ret == null);
        assertTrue( ret instanceof CardDrawNode );

        CardDrawNode cNode = (CardDrawNode)ret;

        assertEquals(cNode.getNumCardsToDraw(), 1);
        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 3);
        assertEquals(waitingPlayer.getMana(), 7);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 4);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getHealth(), health1 - 1);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health1 - 1);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), attack0);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), attack0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), attack0);
    }

    @Test
    public void test3() throws HSException {

        currentPlayer.setMana((byte) 5);
        waitingPlayer.setMana((byte) 5);

        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
        List<HearthActionBoardPair> ab = ai0.playTurn(0, board.data_);
        BoardModel resBoard = ab.get(ab.size() - 1).board;

        assertEquals(resBoard.getCurrentPlayer().getHand().size(), 1); //1 card drawn from GnomishInventor, not enough mana to play it
        assertEquals(resBoard.modelForSide(PlayerSide.CURRENT_PLAYER).getNumMinions(), 3);
        assertEquals(resBoard.modelForSide(PlayerSide.WAITING_PLAYER).getNumMinions(), 1); //1 minion should have been killed
        assertEquals(resBoard.getCurrentPlayer().getMana(), 1); //4 mana used for Gnomish Inventor
        assertEquals(resBoard.getWaitingPlayer().getMana(), 5);
        assertEquals(resBoard.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(resBoard.getWaitingPlayer().getHero().getHealth(), 25); //smacked in the face once
    }

    @Test
    public void test4() throws HSException {

        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
        List<HearthActionBoardPair> ab = ai0.playTurn(0, board.data_);
        BoardModel resBoard = ab.get(ab.size() - 1).board;

        assertEquals(resBoard.getCurrentPlayer().getHand().size(), 0); //1 card drawn from GnomishInventor, and had enough mana to play it
        assertEquals(resBoard.modelForSide(PlayerSide.CURRENT_PLAYER).getNumMinions(), 4);
        assertEquals(resBoard.modelForSide(PlayerSide.WAITING_PLAYER).getNumMinions(), 1); //1 minion should have been killed
        assertEquals(resBoard.getCurrentPlayer().getMana(), 1); //4 mana used for Gnomish Inventor, 2 for Bloodfen Raptor
        assertEquals(resBoard.getWaitingPlayer().getMana(), 7);
        assertEquals(resBoard.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(resBoard.getWaitingPlayer().getHero().getHealth(), 25); //smacked in the face once
    }
}
