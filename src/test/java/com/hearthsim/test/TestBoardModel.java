package com.hearthsim.test;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.BoulderfistOgre;
import com.hearthsim.card.basic.minion.RaidLeader;
import com.hearthsim.card.classic.minion.common.AbusiveSergeant;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionMock;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Test;

import static org.junit.Assert.*;

@SuppressWarnings("EqualsWithItself")
public class TestBoardModel {

    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private static Minion CreateMinionAlpha() {
        return new MinionMock("alpha", (byte)2, (byte)1, (byte)4, (byte)1, (byte)4, (byte)4);
    }

    private static Minion CreateMinionBeta() {
        return new MinionMock("beta", (byte)2, (byte)1, (byte)5, (byte)1, (byte)5, (byte)4);
    }

    private static Minion CreateMinionCharlie() {
        return new MinionMock("charlie", (byte)2, (byte)1, (byte)4, (byte)1, (byte)4, (byte)4);
    }

    @Test
    public void testEqualsEmpty() throws HSException {
        BoardModel board1 = new BoardModel();
        BoardModel board2 = new BoardModel();
        assertEquals(board1, board2);
    }

    @Test
    public void testEqualsWithCardHand() throws HSException {
        BoardModel board1 = new BoardModel();
        BoardModel board2 = new BoardModel();
        BoardModel board3 = new BoardModel();

        board1.getCurrentPlayer().placeCardHand(TestBoardModel.CreateMinionAlpha());
        board3.getCurrentPlayer().placeCardHand(TestBoardModel.CreateMinionAlpha());

        assertNotEquals(board1, board2);
        assertEquals(board1, board3);
    }

    @Test
    public void testEqualsWithPlacedMinions() throws HSException {
        BoardModel board1 = new BoardModel();
        BoardModel board2 = new BoardModel();
        BoardModel board3 = new BoardModel();

        board1.placeMinion(PlayerSide.CURRENT_PLAYER, TestBoardModel.CreateMinionAlpha());
        board3.placeMinion(PlayerSide.CURRENT_PLAYER, TestBoardModel.CreateMinionAlpha());
        assertEquals(board1, board3);

        board2.placeMinion(PlayerSide.CURRENT_PLAYER, TestBoardModel.CreateMinionAlpha());
        board2.placeMinion(PlayerSide.CURRENT_PLAYER, TestBoardModel.CreateMinionBeta());
        assertNotEquals(board1, board2);
    }

    @Test
    public void testEqualsWithMultiplePlacedMinions() throws HSException {
        BoardModel board1 = new BoardModel();
        BoardModel board3 = new BoardModel();

        board1.placeMinion(PlayerSide.CURRENT_PLAYER, TestBoardModel.CreateMinionAlpha());
        board3.placeMinion(PlayerSide.CURRENT_PLAYER, TestBoardModel.CreateMinionAlpha());

        board1.placeMinion(PlayerSide.WAITING_PLAYER, TestBoardModel.CreateMinionAlpha());
        board3.placeMinion(PlayerSide.WAITING_PLAYER, TestBoardModel.CreateMinionAlpha());

        board1.placeMinion(PlayerSide.WAITING_PLAYER, TestBoardModel.CreateMinionCharlie());
        board3.placeMinion(PlayerSide.WAITING_PLAYER, TestBoardModel.CreateMinionCharlie());
        assertEquals(board1, board3);
    }

    @Test
    public void testBoardState() throws HSException {

        int numBoards = 2000;
        BoardModel[] boards = new BoardModel[numBoards];

        int numCards1 = 1;
        int numCards2 = 1;
        int numCards3 = 2;

        Card[] cards1 = new Card[numCards1];
        Card[] cards2 = new Card[numCards2];
        Card[] cards3 = new Card[numCards3];

        for (int i = 0; i < numCards1; ++i) {
            byte attack = (byte)((int)(Math.random() * 6) + 1);
            byte health = (byte)((int)(Math.random() * 2) + 1);
            byte mana = (byte)((int)(0.5 * (attack + health)));

            cards1[i] = new MinionMock("" + i, mana, attack, health, attack, health, health);
        }

        for (int i = 0; i < numCards2; ++i) {
            byte attack = (byte)((int)(Math.random() * 6) + 1);
            byte health = (byte)((int)(Math.random() * 2) + 1);
            byte mana = (byte)((int)(0.5 * (attack + health)));

            cards2[i] = new MinionMock("" + i, mana, attack, health, attack, health, health);
        }

        for (int i = 0; i < numCards3; ++i) {
            byte attack = (byte)((int)(Math.random() * 6) + 1);
            byte health = (byte)((int)(Math.random() * 2) + 1);
            byte mana = (byte)((int)(0.5 * (attack + health)));

            cards3[i] = new MinionMock("" + i, mana, attack, health, attack, health, health);
        }

        for (int i = 0; i < numBoards; ++i) {
            boards[i] = new BoardModel();

            int nh = (int)(Math.random() * 1) + 1;
            int nm1 = (int)(Math.random() * 1) + 1;
            int nm2 = (int)(Math.random() * 2) + 1;

            for (int j = 0; j < nh; ++j) {
                boards[i].getCurrentPlayer().placeCardHand(cards1[(int)(Math.random() * numCards1)]);
            }

            for (int j = 0; j < nm1; ++j) {
                boards[i].placeMinion(PlayerSide.CURRENT_PLAYER, (Minion)cards2[(int)(Math.random() * numCards2)]);
            }

            for (int j = 0; j < nm2; ++j) {
                boards[i].placeMinion(PlayerSide.WAITING_PLAYER, (Minion)cards3[(int)(Math.random() * numCards3)]);
            }
        }

        long nT = 0;
        long nA = 0;
        for (int i = 0; i < numBoards; ++i) {
            for (int j = i + 1; j < numBoards; ++j) {
                boolean res = boards[i].equals(boards[j]);
                if (res)
                    nT = nT + 1;
                nA = nA + 1;
            }
        }
        log.info("t frac = " + (double)nT / (double)nA);
    }

    @Test
    public void testBoardModelEquals() throws HSException {
        HearthTreeNode board = new HearthTreeNode(new BoardModel());
        PlayerModel currentPlayer = board.data_.getCurrentPlayer();
        PlayerModel waitingPlayer = board.data_.getWaitingPlayer();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BoulderfistOgre());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new AbusiveSergeant());

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new BoulderfistOgre());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new AbusiveSergeant());

        currentPlayer.setMana((byte) 8);
        waitingPlayer.setMana((byte) 8);

        board.data_.resetMana();
        board.data_.resetMinions();

        assertTrue(board.data_.equals(board.data_));
        assertTrue(board.data_.equals(board.data_.deepCopy()));
        assertEquals(board.data_.hashCode(), board.data_.deepCopy().hashCode());
        assertFalse(board.data_.equals(board.data_.flipPlayers().deepCopy()));
    }

    @Test
    public void testBoardModelRemoveMinion() throws HSException {
        HearthTreeNode board = new HearthTreeNode(new BoardModel());
        PlayerModel currentPlayer = board.data_.getCurrentPlayer();
        PlayerModel waitingPlayer = board.data_.getWaitingPlayer();

        Minion boulder = new BoulderfistOgre();
        Minion raid = new RaidLeader();
        Minion abusive = new AbusiveSergeant();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, boulder);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, raid);

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, abusive);

        assertMinionReferences(board.data_);

        board.data_.removeMinion(boulder);
        assertMinionReferences(board.data_);
        assertEquals(CharacterIndex.UNKNOWN, currentPlayer.getIndexForCharacter(boulder));

        board.data_.removeMinion(abusive);
        assertMinionReferences(board.data_);
        assertEquals(CharacterIndex.UNKNOWN, waitingPlayer.getIndexForCharacter(boulder));
    }

    @Test
    public void testBoardModelRemoveIdenticalMinion() throws HSException {
        HearthTreeNode board = new HearthTreeNode(new BoardModel());
        PlayerModel currentPlayer = board.data_.getCurrentPlayer();
        PlayerModel waitingPlayer = board.data_.getWaitingPlayer();

        Minion boulder = new BoulderfistOgre();
        Minion boulder2 = new BoulderfistOgre();
        Minion abusive = new AbusiveSergeant();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, boulder);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, boulder2);

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, abusive);

        assertMinionReferences(board.data_);

        board.data_.removeMinion(boulder2);
        assertMinionReferences(board.data_);
        assertEquals(CharacterIndex.UNKNOWN, currentPlayer.getIndexForCharacter(boulder2));
        assertEquals(CharacterIndex.MINION_1, currentPlayer.getIndexForCharacter(boulder));
    }

    @Test
    public void testBoardModelHandleDeadMinions() throws HSException {
        HearthTreeNode board = new HearthTreeNode(new BoardModel());
        PlayerModel currentPlayer = board.data_.getCurrentPlayer();
        PlayerModel waitingPlayer = board.data_.getWaitingPlayer();

        Minion boulder = new BoulderfistOgre();
        boulder.setHealth((byte)-99);
        Minion raid = new RaidLeader();
        Minion abusive = new AbusiveSergeant();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, boulder);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, raid);

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, abusive);

        BoardStateFactoryBase.handleDeadMinions(board);
        assertMinionReferences(board.data_);
        assertEquals(CharacterIndex.UNKNOWN, currentPlayer.getIndexForCharacter(boulder));
    }

    @Test
    public void testBoardModelHandleMultipleDeadMinions() throws HSException {
        HearthTreeNode board = new HearthTreeNode(new BoardModel());
        PlayerModel currentPlayer = board.data_.getCurrentPlayer();
        PlayerModel waitingPlayer = board.data_.getWaitingPlayer();

        Minion boulder = new BoulderfistOgre();
        boulder.setHealth((byte)-99);
        Minion raid = new RaidLeader();
        raid.setHealth((byte)-99);
        Minion abusive = new AbusiveSergeant();
        abusive.setHealth((byte)-99);

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, boulder);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, raid);

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, abusive);

        BoardStateFactoryBase.handleDeadMinions(board);
        assertMinionReferences(board.data_);
        assertEquals(CharacterIndex.UNKNOWN, currentPlayer.getIndexForCharacter(boulder));
        assertEquals(CharacterIndex.UNKNOWN, currentPlayer.getIndexForCharacter(raid));
        assertEquals(CharacterIndex.UNKNOWN, currentPlayer.getIndexForCharacter(abusive));
    }

    @Test
    public void testBoardModelHandleIdenticalDeadMinions() throws HSException {
        HearthTreeNode board = new HearthTreeNode(new BoardModel());
        PlayerModel currentPlayer = board.data_.getCurrentPlayer();
        PlayerModel waitingPlayer = board.data_.getWaitingPlayer();

        Minion boulder = new BoulderfistOgre();
        boulder.setHealth((byte)-99);
        Minion boulder2 = new BoulderfistOgre();
        boulder2.setHealth((byte)-99);
        Minion abusive = new AbusiveSergeant();
        abusive.setHealth((byte)-99);

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, boulder);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, boulder2);

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, abusive);

        BoardStateFactoryBase.handleDeadMinions(board);
        assertMinionReferences(board.data_);
        assertEquals(CharacterIndex.UNKNOWN, currentPlayer.getIndexForCharacter(boulder));
        assertEquals(CharacterIndex.UNKNOWN, currentPlayer.getIndexForCharacter(boulder2));
        assertEquals(CharacterIndex.UNKNOWN, currentPlayer.getIndexForCharacter(abusive));
    }

    @Test
    public void testBoardModelFlipPlayers() throws HSException {
        HearthTreeNode board = new HearthTreeNode(new BoardModel());
        PlayerModel currentPlayer = board.data_.getCurrentPlayer();
        PlayerModel waitingPlayer = board.data_.getWaitingPlayer();

        Minion boulder = new BoulderfistOgre();
        Minion raid = new RaidLeader();
        Minion abusive = new AbusiveSergeant();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, boulder);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, raid);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, abusive);

        BoardModel flipped = board.data_.flipPlayers();
        assertMinionReferences(board.data_);
        assertMinionReferences(flipped);
    }

    public void assertMinionReferences(BoardModel model) {
        for (BoardModel.MinionPlayerPair pair : model.getAllMinionsFIFOList()) {
            assertNotEquals(CharacterIndex.UNKNOWN, model.modelForSide(pair.getPlayerSide()).getIndexForCharacter(pair.getMinion()));
        }

        // TODO also check that each minion in the models exists in FIFO
    }
}
