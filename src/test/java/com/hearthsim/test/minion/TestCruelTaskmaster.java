package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.*;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestCruelTaskmaster {
    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    private Deck deck;

    @Before
    public void setup() throws HSException {
        Card cards[] = new Card[10];
        for (int index = 0; index < 10; ++index) {
            cards[index] = new TheCoin();
        }

        deck = new Deck(cards);

        board = new HearthTreeNode(new BoardModel(deck, deck));
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        Minion minion0_0 = new StormwindChampion();
        Minion minion0_1 = new RaidLeader();
        Minion minion0_2 = new HarvestGolem();

        Minion minion1_0 = new BoulderfistOgre();
        Minion minion1_1 = new RaidLeader();
        Minion minion1_2 = new Abomination();
        Minion minion1_3 = new LootHoarder();

        currentPlayer.placeCardHand(minion0_0);
        currentPlayer.placeCardHand(minion0_1);
        currentPlayer.placeCardHand(minion0_2);

        waitingPlayer.placeCardHand(minion1_0);
        waitingPlayer.placeCardHand(minion1_1);
        waitingPlayer.placeCardHand(minion1_2);
        waitingPlayer.placeCardHand(minion1_3);

        currentPlayer.setMana((byte) 20);
        waitingPlayer.setMana((byte) 20);

        currentPlayer.setMaxMana((byte) 10);
        waitingPlayer.setMaxMana((byte) 10);

        HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
        tmpBoard.data_.getCurrentPlayer().getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayer().getHero(), tmpBoard, deck, null);
        tmpBoard.data_.getCurrentPlayer().getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayer().getHero(), tmpBoard, deck, null);
        tmpBoard.data_.getCurrentPlayer().getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayer().getHero(), tmpBoard, deck, null);
        tmpBoard.data_.getCurrentPlayer().getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayer().getHero(), tmpBoard, deck, null);

        board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        currentPlayer.getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, currentPlayer.getHero(), board, deck, null);
        currentPlayer.getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, currentPlayer.getHero(), board, deck, null);
        currentPlayer.getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, currentPlayer.getHero(), board, deck, null);

        board.data_.resetMana();
        board.data_.resetMinions();

        Minion fb = new CruelTaskmaster();
        currentPlayer.placeCardHand(fb);
    }

    @Test
    public void test0() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 0, board, deck, deck);

        assertNull(ret);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 4);
        assertEquals(currentPlayer.getMana(), 10);
        assertEquals(waitingPlayer.getMana(), 10);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);

        assertEquals(currentPlayer.getMinions().get(0).getTotalHealth(), 4);
        assertEquals(currentPlayer.getMinions().get(1).getTotalHealth(), 3);
        assertEquals(currentPlayer.getMinions().get(2).getTotalHealth(), 6);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalHealth(), 1);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalHealth(), 4);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(3).getTotalHealth(), 7);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 4);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 3);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 3);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(3).getTotalAttack(), 7);
    }

    @Test
    public void test1() throws HSException {

        //set the remaining total health of Abomination to 1
        waitingPlayer.getMinions().get(1).setHealth((byte)1);

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, deck, deck);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 4);
        assertEquals(waitingPlayer.getNumMinions(), 4);
        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(waitingPlayer.getMana(), 10);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);

        assertEquals(currentPlayer.getMinions().get(0).getTotalHealth(), 3);
        assertEquals(currentPlayer.getMinions().get(1).getTotalHealth(), 4);
        assertEquals(currentPlayer.getMinions().get(2).getTotalHealth(), 3);
        assertEquals(currentPlayer.getMinions().get(3).getTotalHealth(), 6);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalHealth(), 1);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalHealth(), 1);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(3).getTotalHealth(), 7);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 4);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 4);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 3);
        assertEquals(currentPlayer.getMinions().get(3).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 3);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(3).getTotalAttack(), 7);

        assertEquals(ret.numChildren(), 7);

        //Forth child node is the one that kills the Loot Hoarder
        HearthTreeNode cn3 = ret.getChildren().get(3);
        assertEquals(cn3.data_.getCurrentPlayer().getHand().size(), 0);
        assertEquals(cn3.data_.getWaitingPlayer().getHand().size(), 1);
        assertEquals(cn3.data_.getCurrentPlayer().getNumMinions(), 4);
        assertEquals(cn3.data_.getWaitingPlayer().getNumMinions(), 3);
        assertEquals(cn3.data_.getCurrentPlayer().getMana(), 8);
        assertEquals(cn3.data_.getWaitingPlayer().getMana(), 10);
        assertEquals(cn3.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(cn3.data_.getWaitingPlayer().getHero().getHealth(), 30);

        assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(0).getTotalHealth(), 3);
        assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(1).getTotalHealth(), 4);
        assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(2).getTotalHealth(), 3);
        assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(3).getTotalHealth(), 6);
//        assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(0).getTotalHealth(), 1);
        assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(0).getTotalHealth(), 1);
        assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(1).getTotalHealth(), 2);
        assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(2).getTotalHealth(), 7);

        assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 4);
        assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 4);
        assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 3);
        assertEquals(cn3.data_.getCurrentPlayer().getMinions().get(3).getTotalAttack(), 7);
//        assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 3);
        assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 5);
        assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), 2);
        assertEquals(cn3.data_.getWaitingPlayer().getMinions().get(2).getTotalAttack(), 7);

        //Fifth child node is the one that kills the Abomination
        HearthTreeNode cn4 = ret.getChildren().get(4);
        assertEquals(cn4.data_.getCurrentPlayer().getHand().size(), 0);
        assertEquals(cn4.data_.getWaitingPlayer().getHand().size(), 1);
        assertEquals(cn4.data_.getCurrentPlayer().getNumMinions(), 4);
        assertEquals(cn4.data_.getWaitingPlayer().getNumMinions(), 1);
        assertEquals(cn4.data_.getCurrentPlayer().getMana(), 8);
        assertEquals(cn4.data_.getWaitingPlayer().getMana(), 10);
        assertEquals(cn4.data_.getCurrentPlayer().getHero().getHealth(), 28);
        assertEquals(cn4.data_.getWaitingPlayer().getHero().getHealth(), 28);

        assertEquals(cn4.data_.getCurrentPlayer().getMinions().get(0).getTotalHealth(), 1);
        assertEquals(cn4.data_.getCurrentPlayer().getMinions().get(1).getTotalHealth(), 2);
        assertEquals(cn4.data_.getCurrentPlayer().getMinions().get(2).getTotalHealth(), 1);
        assertEquals(cn4.data_.getCurrentPlayer().getMinions().get(3).getTotalHealth(), 4);
        assertEquals(cn4.data_.getWaitingPlayer().getMinions().get(0).getTotalHealth(), 5);

        assertEquals(cn4.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), 4);
        assertEquals(cn4.data_.getCurrentPlayer().getMinions().get(1).getTotalAttack(), 4);
        assertEquals(cn4.data_.getCurrentPlayer().getMinions().get(2).getTotalAttack(), 3);
        assertEquals(cn4.data_.getCurrentPlayer().getMinions().get(3).getTotalAttack(), 7);
        assertEquals(cn4.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), 6);
    }
}
