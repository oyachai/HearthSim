package com.hearthsim.test.heroes;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.card.minion.heroes.Warlock;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.card.spellcard.concrete.WildGrowth;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestWarlock {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    private Deck deck;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel(new Warlock(), new TestHero()));
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        Minion minion0_0 = new BoulderfistOgre();
        Minion minion0_1 = new RaidLeader();
        Minion minion1_0 = new BoulderfistOgre();
        Minion minion1_1 = new RaidLeader();

        currentPlayer.placeCardHand(minion0_0);
        currentPlayer.placeCardHand(minion0_1);

        waitingPlayer.placeCardHand(minion1_0);
        waitingPlayer.placeCardHand(minion1_1);

        Card cards[] = new Card[30];
        for (int index = 0; index < 30; ++index) {
            cards[index] = new TheCoin();
        }

        deck = new Deck(cards);

        Card fb = new WildGrowth();
        currentPlayer.placeCardHand(fb);

        currentPlayer.setMana((byte) 9);
        waitingPlayer.setMana((byte) 9);

        currentPlayer.setMaxMana((byte) 8);
        waitingPlayer.setMaxMana((byte) 8);

        HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
        tmpBoard.data_.getCurrentPlayer().getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER,
                tmpBoard.data_.getCurrentPlayer().getHero(), tmpBoard, deck, null);
        tmpBoard.data_.getCurrentPlayer().getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER,
                tmpBoard.data_.getCurrentPlayer().getHero(), tmpBoard, deck, null);

        board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        currentPlayer.getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, currentPlayer.getHero(),
                board, deck, null);
        currentPlayer.getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, currentPlayer.getHero(),
                board, deck, null);

        board.data_.resetMana();
        board.data_.resetMinions();
    }

    @Test
    public void testHeropower() throws HSException {
        Minion target = currentPlayer.getCharacter(0);
        Hero warrior = currentPlayer.getHero();

        HearthTreeNode ret = warrior.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
        assertNotEquals(board, ret);
        assertTrue(ret instanceof CardDrawNode);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getMana(), 6);
        assertEquals(currentPlayer.getHero().getHealth(), 28);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(((CardDrawNode)ret).getNumCardsToDraw(), 1);
        assertEquals(currentPlayer.getMana(), 6);
        assertEquals(currentPlayer.getHero().getHealth(), 28);
    }

    @Test
    public void testHeropowerCannotTargetMinion() throws HSException {
        Minion target = waitingPlayer.getCharacter(2);
        Hero warrior = currentPlayer.getHero();

        HearthTreeNode ret = warrior.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, deck, null);
        assertNull(ret);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);

        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 7);
    }

    @Test
    public void testHeropowerCannotTargetOpponent() throws HSException {
        Minion target = waitingPlayer.getCharacter(0);
        Hero warrior = currentPlayer.getHero();

        HearthTreeNode ret = warrior.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, deck, null);
        assertNull(ret);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
    }

    @Test
    public void testAiCardDrawScore() throws HSException {

        Minion target = waitingPlayer.getCharacter(0);
        Minion minion = currentPlayer.getMinions().get(0);
        HearthTreeNode ret = minion.attack(PlayerSide.WAITING_PLAYER, target, board, deck, null, false);
        assertEquals(board, ret);

        currentPlayer.setDeckPos((byte) 30);

        target = currentPlayer.getCharacter(0);
        Hero hero = currentPlayer.getHero();
        ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
        assertEquals(currentPlayer.getHand().size(), 1);

        assertTrue(ret instanceof CardDrawNode);
        assertEquals(((CardDrawNode)ret).getNumCardsToDraw(), 1);

        BruteForceSearchAI ai0 = BruteForceSearchAI.buildStandardAI1();
        double cardDrawScore = ((CardDrawNode)ret).cardDrawScore(deck, ai0.scorer);
        assertTrue(cardDrawScore < 0.0);
    }
}
