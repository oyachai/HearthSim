package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.*;
import com.hearthsim.card.spellcard.concrete.Fireball;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.IdentityLinkedList;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestCairneBloodhoof {


    private HearthTreeNode board;
    private Deck deck;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());

        Minion minion0_0 = new BoulderfistOgre();
        Minion minion0_1 = new RaidLeader();
        Minion minion1_0 = new BoulderfistOgre();
        Minion minion1_1 = new RaidLeader();

        board.data_.placeCardHandCurrentPlayer(minion0_0);
        board.data_.placeCardHandCurrentPlayer(minion0_1);

        board.data_.placeCardHandWaitingPlayer(minion1_0);
        board.data_.placeCardHandWaitingPlayer(minion1_1);

        Card cards[] = new Card[10];
        for (int index = 0; index < 10; ++index) {
            cards[index] = new TheCoin();
        }

        deck = new Deck(cards);

        Card fb = new CairneBloodhoof();
        board.data_.placeCardHandCurrentPlayer(fb);

        board.data_.getCurrentPlayer().setMana((byte)18);
        board.data_.getWaitingPlayer().setMana((byte)18);

        board.data_.getCurrentPlayer().setMaxMana((byte)8);
        board.data_.getWaitingPlayer().setMaxMana((byte)8);

        HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
        tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayer().getHero(), tmpBoard, deck, null);
        tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayer().getHero(), tmpBoard, deck, null);

        board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
        board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayer().getHero(), board, deck, null);
        board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayer().getHero(), board, deck, null);

        board.data_.resetMana();
        board.data_.resetMinions();

    }



    @Test
    public void test0() throws HSException {
        Card theCard = board.data_.getCurrentPlayerCardHand(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 0, board, deck, null);

        assertNull(ret);
        PlayerModel currentPlayer = board.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = board.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        assertEquals(board.data_.getNumCards_hand(), 1);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(board.data_.getCurrentPlayer().getMana(), 8);
        assertEquals(board.data_.getWaitingPlayer().getMana(), 8);
        assertEquals(board.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(board.data_.getWaitingPlayer().getHero().getHealth(), 30);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 7);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 7);
    }

    @Test
    public void test2() throws HSException {
        Card theCard = board.data_.getCurrentPlayerCardHand(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 2, board, deck, null);

        assertFalse(ret == null);
        PlayerModel currentPlayer = board.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = board.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        assertEquals(board.data_.getNumCards_hand(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(board.data_.getCurrentPlayer().getMana(), 2);
        assertEquals(board.data_.getWaitingPlayer().getMana(), 8);
        assertEquals(board.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(board.data_.getWaitingPlayer().getHero().getHealth(), 30);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 7);
        assertEquals(currentPlayer.getMinions().get(2).getHealth(), 5);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 7);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 7);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 7);



        //----------------------------------------------------------

        Minion minion = currentPlayer.getMinions().get(2);
        assertTrue(minion instanceof CairneBloodhoof);

        minion.hasAttacked(false);
        Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 2);
        minion.attack(PlayerSide.WAITING_PLAYER, target, board, deck, null, false);

        assertEquals(board.data_.getNumCards_hand(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(board.data_.getCurrentPlayer().getMana(), 2);
        assertEquals(board.data_.getWaitingPlayer().getMana(), 8);
        assertEquals(board.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(board.data_.getWaitingPlayer().getHero().getHealth(), 30);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 7);
        assertEquals(currentPlayer.getMinions().get(2).getHealth(), 5);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 7 - 5);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 7);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 7);

        assertTrue(currentPlayer.getMinions().get(2) instanceof BaineBloodhoof);

    }


    @Test
    public void test3() throws HSException {
        Card theCard = board.data_.getCurrentPlayerCardHand(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board, deck, null);

        assertFalse(ret == null);
        PlayerModel currentPlayer = board.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = board.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        assertEquals(board.data_.getNumCards_hand(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(board.data_.getCurrentPlayer().getMana(), 2);
        assertEquals(board.data_.getWaitingPlayer().getMana(), 8);
        assertEquals(board.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(board.data_.getWaitingPlayer().getHero().getHealth(), 30);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 5);
        assertEquals(currentPlayer.getMinions().get(2).getHealth(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 7);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 5);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 7);



        //----------------------------------------------------------

        Minion minion = currentPlayer.getMinions().get(1);
        assertTrue(minion instanceof CairneBloodhoof);

        minion.hasAttacked(false);
        Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 2);
        minion.attack(PlayerSide.WAITING_PLAYER, target, board, deck, null, false);

        assertEquals(board.data_.getNumCards_hand(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(board.data_.getCurrentPlayer().getMana(), 2);
        assertEquals(board.data_.getWaitingPlayer().getMana(), 8);
        assertEquals(board.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(board.data_.getWaitingPlayer().getHero().getHealth(), 30);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 5);
        assertEquals(currentPlayer.getMinions().get(2).getHealth(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 7 - 5);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 5);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 7);

        assertTrue(currentPlayer.getMinions().get(1) instanceof BaineBloodhoof);

    }

    @Test
    public void test4() throws HSException {
        Card theCard = board.data_.getCurrentPlayerCardHand(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board, deck, null);

        assertFalse(ret == null);
        PlayerModel currentPlayer = board.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = board.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        assertEquals(board.data_.getNumCards_hand(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(board.data_.getCurrentPlayer().getMana(), 2);
        assertEquals(board.data_.getWaitingPlayer().getMana(), 8);
        assertEquals(board.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(board.data_.getWaitingPlayer().getHero().getHealth(), 30);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 5);
        assertEquals(currentPlayer.getMinions().get(2).getHealth(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 7);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 5);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 7);


        //----------------------------------------------------------
        HearthTreeNode fb = new HearthTreeNode(board.data_.flipPlayers());
        currentPlayer = fb.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        waitingPlayer = fb.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        IdentityLinkedList<Minion> currentPlayerMinions = currentPlayer.getMinions();
        Minion minion = currentPlayerMinions.get(1);

        minion.hasAttacked(false);
        Minion target = fb.data_.getCharacter(PlayerSide.WAITING_PLAYER, 2);
        minion.attack(PlayerSide.WAITING_PLAYER, target, fb, deck, null, false);

        assertEquals(fb.data_.getNumCards_hand(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 3);
        assertEquals(fb.data_.getCurrentPlayer().getMana(), 8);
        assertEquals(fb.data_.getWaitingPlayer().getMana(), 2);
        assertEquals(fb.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(fb.data_.getWaitingPlayer().getHero().getHealth(), 30);
        assertEquals(currentPlayerMinions.get(0).getHealth(), 2);
        assertEquals(currentPlayerMinions.get(1).getHealth(), 7 - 5);
        IdentityLinkedList<Minion> waitingPlayerMinions = waitingPlayer.getMinions();
        assertEquals(waitingPlayerMinions.get(0).getHealth(), 2);
        assertEquals(waitingPlayerMinions.get(1).getHealth(), 5);
        assertEquals(waitingPlayerMinions.get(2).getHealth(), 7);

        assertEquals(currentPlayerMinions.get(0).getTotalAttack(), 2);
        assertEquals(currentPlayerMinions.get(1).getTotalAttack(), 7);
        assertEquals(waitingPlayerMinions.get(0).getTotalAttack(), 2);
        assertEquals(waitingPlayerMinions.get(1).getTotalAttack(), 5);
        assertEquals(waitingPlayerMinions.get(2).getTotalAttack(), 7);

        assertTrue(waitingPlayerMinions.get(1) instanceof BaineBloodhoof);
    }

    @Test
    public void test5() throws HSException {
        Card theCard = board.data_.getCurrentPlayerCardHand(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board, deck, null);

        ret = new BloodfenRaptor().summonMinion(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayer().getHero(), ret, null, null, false, true);
        ret = new BloodfenRaptor().summonMinion(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayer().getHero(), ret, null, null, false, true);
        ret = new BloodfenRaptor().summonMinion(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayer().getHero(), ret, null, null, false, true);
        ret = new BloodfenRaptor().summonMinion(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayer().getHero(), ret, null, null, false, true);

        assertFalse(ret == null);
        PlayerModel currentPlayer = board.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = board.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        assertEquals(board.data_.getNumCards_hand(), 0);
        assertEquals(currentPlayer.getNumMinions(), 7);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(board.data_.getCurrentPlayer().getMana(), 2);
        assertEquals(board.data_.getWaitingPlayer().getMana(), 8);
        assertEquals(board.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(board.data_.getWaitingPlayer().getHero().getHealth(), 30);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(2).getHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(3).getHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(4).getHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(5).getHealth(), 5);
        assertEquals(currentPlayer.getMinions().get(6).getHealth(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 7);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 4);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 4);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 4);
        assertEquals(currentPlayer.getMinions().get(3).getTotalAttack(), 4);
        assertEquals(currentPlayer.getMinions().get(4).getTotalAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(5).getTotalAttack(), 5);
        assertEquals(currentPlayer.getMinions().get(6).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 7);


        //----------------------------------------------------------
        HearthTreeNode fb = new HearthTreeNode(board.data_.flipPlayers());
        currentPlayer = fb.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        waitingPlayer = fb.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        Minion minion = currentPlayer.getMinions().get(1);

        minion.hasAttacked(false);
        Minion target = fb.data_.getCharacter(PlayerSide.WAITING_PLAYER, 6);
        minion.attack(PlayerSide.WAITING_PLAYER, target, fb, deck, null, false);

        assertEquals(fb.data_.getNumCards_hand(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 7);
        assertEquals(fb.data_.getCurrentPlayer().getMana(), 8);
        assertEquals(fb.data_.getWaitingPlayer().getMana(), 2);
        assertEquals(fb.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(fb.data_.getWaitingPlayer().getHero().getHealth(), 30);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 7 - 5);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(2).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(3).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(4).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(5).getHealth(), 5);
        assertEquals(waitingPlayer.getMinions().get(6).getHealth(), 7);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getMinions().get(3).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getMinions().get(4).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(5).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getMinions().get(6).getTotalAttack(), 7);

        assertTrue(waitingPlayer.getMinions().get(5) instanceof BaineBloodhoof);

    }


    @Test
    public void testKillCairneWithFireball() throws HSException {
        Card theCard = board.data_.getCurrentPlayerCardHand(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board, deck, null);

        assertFalse(ret == null);
        PlayerModel currentPlayer = board.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = board.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        assertEquals(board.data_.getNumCards_hand(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(board.data_.getCurrentPlayer().getMana(), 2);
        assertEquals(board.data_.getWaitingPlayer().getMana(), 8);
        assertEquals(board.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(board.data_.getWaitingPlayer().getHero().getHealth(), 30);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 5);
        assertEquals(currentPlayer.getMinions().get(2).getHealth(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 7);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 5);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 7);


        //----------------------------------------------------------
        HearthTreeNode fb = new HearthTreeNode(board.data_.flipPlayers());
        currentPlayer = fb.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        waitingPlayer = fb.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        Minion minion = currentPlayer.getMinions().get(1);

        fb.data_.placeCardHandCurrentPlayer(new Fireball());
        Card fireball = fb.data_.getCurrentPlayerCardHand(0);

        minion.hasAttacked(false);
        fireball.useOn(PlayerSide.WAITING_PLAYER, 2, fb, deck, null);

        assertEquals(fb.data_.getNumCards_hand(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 3);
        assertEquals(fb.data_.getCurrentPlayer().getMana(), 4);
        assertEquals(fb.data_.getWaitingPlayer().getMana(), 2);
        assertEquals(fb.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(fb.data_.getWaitingPlayer().getHero().getHealth(), 30);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 5);
        assertEquals(waitingPlayer.getMinions().get(2).getHealth(), 7);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalAttack(), 7);

        assertTrue(waitingPlayer.getMinions().get(1) instanceof BaineBloodhoof);
    }
}
