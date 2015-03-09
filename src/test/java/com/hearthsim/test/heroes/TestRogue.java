package com.hearthsim.test.heroes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.hearthsim.model.PlayerModel;
import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.minion.heroes.Rogue;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.card.weapon.concrete.AssassinsBlade;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestRogue {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    private Deck deck;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel(new Rogue(), new TestHero()));
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

        Card cards[] = new Card[10];
        for (int index = 0; index < 10; ++index) {
            cards[index] = new TheCoin();
        }

        deck = new Deck(cards);

        Card fb = new AssassinsBlade();
        currentPlayer.placeCardHand(fb);

        currentPlayer.setMana((byte) 9);
        waitingPlayer.setMana((byte) 9);

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
    public void testHeropower() throws HSException {
        Hero hero = currentPlayer.getHero();
        Minion target = currentPlayer.getCharacter(0); // Rogue hero
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
        assertEquals(board, ret);

        assertTrue(hero.hasBeenUsed());
        assertEquals(currentPlayer.getMana(), 6);
        assertEquals(currentPlayer.getHero().getWeapon().getWeaponCharge(), 2);
        assertEquals(currentPlayer.getHero().getTotalAttack(), 1);
    }

    @Test
    public void testHeropowerDestroysEquippedWeapon() throws HSException {
        currentPlayer.getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, currentPlayer.getHero(), board, deck, null);
        assertEquals(currentPlayer.getMana(), 3);
        assertEquals(currentPlayer.getHero().getWeapon().getWeaponCharge(), 4);
        assertEquals(currentPlayer.getHero().getTotalAttack(), 3);

        Hero hero = currentPlayer.getHero();
        Minion target = currentPlayer.getCharacter(0); // Rogue hero
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
        assertEquals(board, ret);

        assertTrue(hero.hasBeenUsed());
        assertEquals(currentPlayer.getMana(), 1);
        assertEquals(currentPlayer.getHero().getWeapon().getWeaponCharge(), 2);
        assertEquals(currentPlayer.getHero().getTotalAttack(), 1);
    }

    @Test
    public void testCannotTargetMinion() throws HSException {
        Minion raidLeader = currentPlayer.getCharacter(1); // Raid leader
        Hero hero = currentPlayer.getHero();
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, raidLeader, board, deck, null);
        assertNull(ret);

        assertFalse(hero.hasBeenUsed());
        assertEquals(currentPlayer.getMana(), 8);
        assertNull(currentPlayer.getHero().getWeapon());
        assertEquals(currentPlayer.getHero().getTotalAttack(), 0);

        assertEquals(currentPlayer.getMinions().get(0).getAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 2);
    }

    @Test
    public void testCannotTargetOpponent() throws HSException {
        Minion target = waitingPlayer.getCharacter(0);
        Hero hero = currentPlayer.getHero();
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, deck, null);
        assertNull(ret);

        assertFalse(hero.hasBeenUsed());
        assertEquals(currentPlayer.getMana(), 8);
        assertNull(currentPlayer.getHero().getWeapon());
        assertEquals(currentPlayer.getHero().getTotalAttack(), 0);

        assertNull(waitingPlayer.getHero().getWeapon());
        assertEquals(waitingPlayer.getHero().getTotalAttack(), 0);
    }
}
