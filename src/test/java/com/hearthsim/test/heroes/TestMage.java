package com.hearthsim.test.heroes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import com.hearthsim.model.PlayerModel;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.ChillwindYeti;
import com.hearthsim.card.minion.concrete.FaerieDragon;
import com.hearthsim.card.minion.heroes.Mage;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestMage {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    private Deck deck;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel(new Mage(), new TestHero()));
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        Minion minion0_0 = new ChillwindYeti();
        Minion minion0_1 = new BoulderfistOgre();
        Minion minion1_0 = new ChillwindYeti();
        Minion minion1_1 = new BoulderfistOgre();

        currentPlayer.placeCardHand(minion0_1);
        currentPlayer.placeCardHand(minion0_0);

        waitingPlayer.placeCardHand(minion1_1);
        waitingPlayer.placeCardHand(minion1_0);

        Card cards[] = new Card[10];
        for (int index = 0; index < 10; ++index) {
            cards[index] = new TheCoin();
        }

        deck = new Deck(cards);

        currentPlayer.setMana((byte) 11);
        waitingPlayer.setMana((byte) 11);

        currentPlayer.setMaxMana((byte) 8);
        waitingPlayer.setMaxMana((byte) 8);

        HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
        tmpBoard.data_.getCurrentPlayer().getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayer().getHero(), tmpBoard, deck, null);
        tmpBoard.data_.getCurrentPlayer().getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayer().getHero(), tmpBoard, deck, null);

        board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        currentPlayer.getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, currentPlayer.getHero(), board, deck, null);
        currentPlayer.getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, currentPlayer.getHero(), board, deck, null);

        board.data_.resetMana();
        board.data_.resetMinions();
    }

    @Test
    public void testHeropowerAgainstOpponent() throws HSException {
        Minion target = waitingPlayer.getCharacter(0); // Opponent hero
        Hero mage = currentPlayer.getHero();

        HearthTreeNode ret = mage.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, deck, null);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getMana(), 6);
        assertEquals(waitingPlayer.getHero().getHealth(), 29);
    }

    @Test
    public void testHeropowerAgainstMinion() throws HSException {
        Minion target = waitingPlayer.getCharacter(1); // Yeti
        Hero mage = currentPlayer.getHero();

        HearthTreeNode ret = mage.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, deck, null);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getMana(), 6);

        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 5);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 4);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 7);
    }

    @Test
    @Ignore("Functionality explicitly disabled")
    public void testHeropowerAgainstSelf() throws HSException {
        Minion target = currentPlayer.getCharacter(0); // Self
        Hero mage = currentPlayer.getHero();

        HearthTreeNode ret = mage.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getMana(), 6);
        assertEquals(currentPlayer.getHero().getHealth(), 29);
    }

    @Test
    public void testHeropowerAgainstOwnMinion() throws HSException {
        Minion target = currentPlayer.getCharacter(1); // Yeti
        Hero mage = currentPlayer.getHero();

        HearthTreeNode ret = mage.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, deck, null);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getMana(), 6);

        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 4);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 5);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 7);
    }

    @Test
    public void testHeropowerAgainstFaerieMinion() throws HSException {
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new FaerieDragon());

        Minion target = waitingPlayer.getCharacter(3); // Faerie
        Hero mage = currentPlayer.getHero();

        assertFalse(mage.canBeUsedOn(PlayerSide.WAITING_PLAYER, target, board.data_));
        HearthTreeNode ret = mage.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, deck, null);
        assertNull(ret);

        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(waitingPlayer.getMinions().get(2).getHealth(), 2);
    }

    @Test
    public void testHeropowerKillsMinion() throws HSException {
        Minion target = waitingPlayer.getCharacter(1); // Yeti
        target.setHealth((byte)1);

        Hero mage = currentPlayer.getHero();

        HearthTreeNode ret = mage.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, deck, null);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 1);

        assertEquals(currentPlayer.getMana(), 6);

        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 5);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 7);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 4);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 6);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 6);
    }
}
