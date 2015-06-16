package com.hearthsim.test.heroes;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.BoulderfistOgre;
import com.hearthsim.card.basic.minion.RaidLeader;
import com.hearthsim.card.basic.weapon.AssassinsBlade;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.heroes.Rogue;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestRogue {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel(new Rogue(), new TestHero()));
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BoulderfistOgre());

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new BoulderfistOgre());

        Card fb = new AssassinsBlade();
        currentPlayer.placeCardHand(fb);

        currentPlayer.setMana((byte) 8);
        waitingPlayer.setMana((byte) 8);
    }

    @Test
    public void testHeropower() throws HSException {
        Hero hero = currentPlayer.getHero();
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        assertTrue(hero.hasBeenUsed());
        assertEquals(currentPlayer.getMana(), 6);
        assertEquals(currentPlayer.getHero().getWeapon().getWeaponCharge(), 2);
        assertEquals(currentPlayer.getHero().getTotalAttack(), 1);
    }

    @Test
    public void testHeropowerDestroysEquippedWeapon() throws HSException {
        currentPlayer.getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(currentPlayer.getMana(), 3);
        assertEquals(currentPlayer.getHero().getWeapon().getWeaponCharge(), 4);
        assertEquals(currentPlayer.getHero().getTotalAttack(), 3);

        Hero hero = currentPlayer.getHero();
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        assertTrue(hero.hasBeenUsed());
        assertEquals(currentPlayer.getMana(), 1);
        assertEquals(currentPlayer.getHero().getWeapon().getWeaponCharge(), 2);
        assertEquals(currentPlayer.getHero().getTotalAttack(), 1);
    }

    @Test
    public void testCannotTargetMinion() throws HSException {
        Hero hero = currentPlayer.getHero();
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1, board);
        assertNull(ret);

        assertFalse(hero.hasBeenUsed());
        assertEquals(currentPlayer.getMana(), 8);
        assertNull(currentPlayer.getHero().getWeapon());
        assertEquals(currentPlayer.getHero().getTotalAttack(), 0);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getAttack(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
    }

    @Test
    public void testCannotTargetOpponent() throws HSException {
        Hero hero = currentPlayer.getHero();
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);
        assertNull(ret);

        assertFalse(hero.hasBeenUsed());
        assertEquals(currentPlayer.getMana(), 8);
        assertNull(currentPlayer.getHero().getWeapon());
        assertEquals(currentPlayer.getHero().getTotalAttack(), 0);

        assertNull(waitingPlayer.getHero().getWeapon());
        assertEquals(waitingPlayer.getHero().getTotalAttack(), 0);
    }
}
