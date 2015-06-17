package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.Deck;
import com.hearthsim.card.basic.minion.BloodfenRaptor;
import com.hearthsim.card.basic.minion.SilverHandRecruit;
import com.hearthsim.card.basic.spell.Assassinate;
import com.hearthsim.card.classic.minion.common.AcolyteOfPain;
import com.hearthsim.card.minion.Minion;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestAcolyteOfPain {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    private AcolyteOfPain acolyteOnBoard;

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

        acolyteOnBoard = new AcolyteOfPain();
        Minion minion1_0 = new SilverHandRecruit();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, acolyteOnBoard);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);

        AcolyteOfPain acolyteInHand = new AcolyteOfPain();
        currentPlayer.placeCardHand(acolyteInHand);

        currentPlayer.setMana((byte) 7);
        waitingPlayer.setMana((byte) 7);
    }

    @Test
    public void testAiDrawsCardFromAcolyte() throws HSException {
        currentPlayer.setMana((byte) 1);

        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
        List<HearthActionBoardPair> ab = ai0.playTurn(0, board.data_);
        BoardModel resBoard = ab.get(ab.size() - 1).board;

        assertEquals(resBoard.getCurrentPlayer().getHand().size(), 2); // 1 card drawn from AcolyteOfPain, not enough mana to play it
        assertEquals(resBoard.modelForSide(PlayerSide.CURRENT_PLAYER).getNumMinions(), 1);
        assertEquals(resBoard.modelForSide(PlayerSide.WAITING_PLAYER).getNumMinions(), 0); // 1 minion should have been killed
        assertEquals(resBoard.getCurrentPlayer().getMana(), 1); // 0 mana used
    }

    @Test
    public void testAiDrawsAndPlaysCardFromAcolyte() throws HSException {
        currentPlayer.setMana((byte) 3);

        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
        List<HearthActionBoardPair> ab = ai0.playTurn(0, board.data_);
        BoardModel resBoard = ab.get(ab.size() - 1).board;

        assertEquals(resBoard.getCurrentPlayer().getHand().size(), 1); // 1 card drawn from AcolyteOfPain, then played the Bloodfen Raptor
        assertEquals(resBoard.modelForSide(PlayerSide.CURRENT_PLAYER).getNumMinions(), 2);
        assertEquals(resBoard.modelForSide(PlayerSide.WAITING_PLAYER).getNumMinions(), 0); // 1 minion should have been killed
        assertEquals(resBoard.getCurrentPlayer().getMana(), 1); // 2 mana used... it's better to put down a Bloodfen Raptor than an Acolyte of Pain
    }

    @Test
    public void testDamagingEnemyAcolyteDrawsCard() throws HSException {
        currentPlayer.setMana((byte) 1);

        board.data_.removeMinion(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_1);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new AcolyteOfPain());

        assertEquals(waitingPlayer.getHand().size(), 0);

        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
        List<HearthActionBoardPair> ab = ai0.playTurn(0, board.data_);
        BoardModel resBoard = ab.get(ab.size() - 1).board;

        assertEquals(resBoard.getCurrentPlayer().getHand().size(), 2); // 1 card drawn from AcolyteOfPain
        assertEquals(resBoard.getWaitingPlayer().getHand().size(), 1); // 1 card drawn from AcolyteOfPain. The Acolytes smack into each other.
        assertEquals(resBoard.modelForSide(PlayerSide.CURRENT_PLAYER).getNumMinions(), 1);
        assertEquals(resBoard.modelForSide(PlayerSide.WAITING_PLAYER).getNumMinions(), 1);
    }

    @Test
    public void testDivineShieldPreventsDraw() throws HSException {
        acolyteOnBoard.setDivineShield(true);
        HearthTreeNode ret = acolyteOnBoard.attack(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_1, board);
        assertEquals(board, ret);

        assertFalse(board instanceof CardDrawNode);
        assertEquals(currentPlayer.getHand().size(), 1);
    }

    @Test
    public void testDestroyingPreventsDraw() throws HSException {
        currentPlayer.setMana((byte) 5);

        AcolyteOfPain enemyAcolyte = new AcolyteOfPain();
        board.data_.removeMinion(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_1);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, enemyAcolyte);

        assertEquals(waitingPlayer.getHand().size(), 0);
        Assassinate assassinate = new Assassinate();
        currentPlayer.placeCardHand(assassinate);
        HearthTreeNode ret = assassinate.useOn(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_1, board);
        assertEquals(board, ret);
        assertFalse(board instanceof CardDrawNode);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(waitingPlayer.getHand().size(), 0);

        assertEquals(waitingPlayer.getNumMinions(), 0);
    }
}
