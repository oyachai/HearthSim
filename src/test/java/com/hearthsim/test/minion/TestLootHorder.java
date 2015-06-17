package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.Deck;
import com.hearthsim.card.basic.minion.BloodfenRaptor;
import com.hearthsim.card.basic.minion.RiverCrocolisk;
import com.hearthsim.card.classic.minion.common.LootHoarder;
import com.hearthsim.card.classic.minion.common.Wisp;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.util.HearthActionBoardPair;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestLootHorder {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        Card cards[] = new Card[10];
        for (int index = 0; index < 10; ++index) {
            cards[index] = new RiverCrocolisk();
        }

        Deck deck = new Deck(cards);
        PlayerModel playerModel0 = new PlayerModel((byte)0, "player0", new TestHero(), deck);
        PlayerModel playerModel1 = new PlayerModel((byte)1, "player1", new TestHero(), deck);

        board = new HearthTreeNode(new BoardModel(playerModel0, playerModel1));
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        Minion minion0_0 = new LootHoarder();
        Minion minion1_0 = new BloodfenRaptor();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);
    }

    @Test
    public void test0() throws HSException {

        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();

        currentPlayer.setMana((byte) 1);
        waitingPlayer.setMana((byte) 1);

        List<HearthActionBoardPair> ab = ai0.playTurn(0, board.data_);
        BoardModel resBoard = ab.get(ab.size() - 1).board;

        assertEquals(resBoard.getCurrentPlayer().getHand().size(), 1); //1 card drawn from Loot Horder attacking and dying, no mana left to play the card
        assertEquals(resBoard.modelForSide(PlayerSide.CURRENT_PLAYER).getNumMinions(), 0);
        assertEquals(resBoard.modelForSide(PlayerSide.WAITING_PLAYER).getNumMinions(), 0); //1 minion should have been killed
        assertEquals(resBoard.getCurrentPlayer().getMana(), 1); //no mana used
        assertEquals(resBoard.getWaitingPlayer().getMana(), 1);
        assertEquals(resBoard.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(resBoard.getWaitingPlayer().getHero().getHealth(), 30);
    }

    @Test
    public void test1() throws HSException {

        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();

        currentPlayer.setMana((byte) 3);
        waitingPlayer.setMana((byte) 3);

        List<HearthActionBoardPair> ab = ai0.playTurn(0, board.data_);
        BoardModel resBoard = ab.get(ab.size() - 1).board;

        assertEquals(resBoard.getCurrentPlayer().getHand().size(), 0); //1 card drawn from Loot Horder attacking and dying, then played the drawn card
        assertEquals(resBoard.modelForSide(PlayerSide.CURRENT_PLAYER).getNumMinions(), 1);
        assertEquals(resBoard.modelForSide(PlayerSide.WAITING_PLAYER).getNumMinions(), 0); //1 minion should have been killed
        assertEquals(resBoard.getCurrentPlayer().getMana(), 1); //2 mana used
        assertEquals(resBoard.getWaitingPlayer().getMana(), 3);
        assertEquals(resBoard.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(resBoard.getWaitingPlayer().getHero().getHealth(), 30);
    }

    @Test
    public void test2() throws HSException {

        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();

        //remove the loot hoarder from player0, add a Wisp
        board.data_.removeMinion(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new Wisp());

        //remove the Bloodfen Raptor from player0, add a Loot Hoarder
        board.data_.removeMinion(PlayerSide.WAITING_PLAYER, CharacterIndex.MINION_1);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new LootHoarder());

        currentPlayer.setMana((byte) 3);
        waitingPlayer.setMana((byte) 3);

        List<HearthActionBoardPair> ab = ai0.playTurn(0, board.data_);
        BoardModel resBoard = ab.get(ab.size() - 1).board;

        assertEquals(resBoard.getCurrentPlayer().getHand().size(), 0); //no cards in hand
        assertEquals(resBoard.getWaitingPlayer().getHand().size(), 1); //drew cards from the loot horder that was killed
        assertEquals(resBoard.modelForSide(PlayerSide.CURRENT_PLAYER).getNumMinions(), 0);
        assertEquals(resBoard.modelForSide(PlayerSide.WAITING_PLAYER).getNumMinions(), 0); //1 minion should have been killed
        assertEquals(resBoard.getCurrentPlayer().getMana(), 3); //no mana used
        assertEquals(resBoard.getWaitingPlayer().getMana(), 3);
        assertEquals(resBoard.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(resBoard.getWaitingPlayer().getHero().getHealth(), 30);
    }
}
