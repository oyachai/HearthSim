package com.hearthsim.test.heroes;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.BoulderfistOgre;
import com.hearthsim.card.basic.minion.KoboldGeomancer;
import com.hearthsim.card.basic.minion.RaidLeader;
import com.hearthsim.card.basic.spell.HolySmite;
import com.hearthsim.card.basic.weapon.FieryWarAxe;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestHeroBase {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        TestHero self = new TestHero();
        self.enableHeroAbility = true;
        board = new HearthTreeNode(new BoardModel(self, new TestHero()));
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BoulderfistOgre());

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new BoulderfistOgre());

        currentPlayer.setMana((byte) 8);
        waitingPlayer.setMana((byte) 8);
    }

    @Test
    public void testHeroCannotAttack() throws HSException {
        Hero hero = currentPlayer.getHero();

        assertFalse(hero.canAttack());

        HearthTreeNode ret = hero.attack(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);
        assertNull(ret);
    }

    @Test
    public void testMinionAttackingHero() throws HSException {

        // null case
        Minion minion = currentPlayer.getCharacter(CharacterIndex.MINION_1);
        HearthTreeNode ret = minion.attack(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 2);

        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 28);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 7);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), 7);

        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getTotalAttack(), 7);
    }

    @Test
    public void testHeropowerIsMarkedUsed() throws HSException {
        Hero hero = currentPlayer.getHero();
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);
        assertTrue(hero.hasBeenUsed());
    }

    @Test
    public void testHeropowerSubtractsMana() throws HSException {
        Hero hero = currentPlayer.getHero();
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);
        assertEquals(currentPlayer.getMana(), 6);
    }

    @Test
    public void testHeropowerCannotBeUsedTwice() throws HSException {
        Hero hero = currentPlayer.getHero();
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        assertFalse(hero.canBeUsedOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board.data_));

        ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertNull(ret);

        assertTrue(hero.hasBeenUsed());
        assertEquals(currentPlayer.getMana(), 6);
    }

    @Test
    public void testHeropowerResets() throws HSException {
        Hero hero = currentPlayer.getHero();
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        board.data_.resetMinions();
        assertFalse(hero.hasBeenUsed());
    }

    @Test
    public void testNotEnoughMana() throws HSException {
        Hero hero = currentPlayer.getHero();
        currentPlayer.setMana((byte) 1);

        //assertFalse(hero.canBeUsedOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board.data_)); // TODO doesn't work yet

        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertNull(ret);

        assertFalse(hero.hasBeenUsed());
        assertEquals(currentPlayer.getMana(), 1);
    }

    @Test
    public void testDeepCopyWeapon() throws HSException {
        Hero hero = currentPlayer.getHero();
        currentPlayer.setMana((byte) 2);

        FieryWarAxe axe = new FieryWarAxe();
        hero.setWeapon(axe);

        Hero copy = hero.deepCopy();

        assertEquals(hero, copy);
        assertEquals(axe, copy.getWeapon());
    }

    @Test
    public void testNotEqualWeapon() throws HSException {
        Hero hero = currentPlayer.getHero();
        currentPlayer.setMana((byte) 2);

        Hero copy = hero.deepCopy();

        FieryWarAxe axe = new FieryWarAxe();
        hero.setWeapon(axe);

        FieryWarAxe otherAxe = new FieryWarAxe();
        otherAxe.setWeaponCharge((byte) 1);
        copy.setWeapon(otherAxe);

        assertNotEquals(hero, copy);
    }

    @Test
    public void testMinionAttackingHeroRemovesArmorFirst() throws HSException {

        Hero opponent = waitingPlayer.getHero();
        opponent.setArmor((byte)3);

        Minion minion = currentPlayer.getCharacter(CharacterIndex.MINION_1);
        HearthTreeNode ret = minion.attack(PlayerSide.WAITING_PLAYER, opponent, board);
        assertEquals(board, ret);

        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getArmor(), 1);
    }

    @Test
    public void testSpellpowerEffectsArmor() throws HSException {
        Hero opponent = waitingPlayer.getHero();
        opponent.setArmor((byte)10);

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new KoboldGeomancer());

        HolySmite smite = new HolySmite();
        board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, smite);

        HearthTreeNode ret = smite.useOn(PlayerSide.WAITING_PLAYER, CharacterIndex.HERO, board);
        assertEquals(board, ret);

        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getArmor(), 7);
    }

    @Test
    public void testFatigueDamage() throws HSException {
        currentPlayer.drawNextCardFromDeck(); // 1 damage
        currentPlayer.drawNextCardFromDeck(); // 2 damage = 3 total

        assertEquals(currentPlayer.getHero().getHealth(), 27);
    }

    @Test
    public void testFatigueDamageEffectsArmorFirst() throws HSException {
        currentPlayer.getHero().setArmor((byte)10);

        currentPlayer.drawNextCardFromDeck(); // 1 damage
        currentPlayer.drawNextCardFromDeck(); // 2 damage = 3 total

        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getHero().getArmor(), 7);
    }
}
