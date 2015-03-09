package com.hearthsim.test.heroes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.hearthsim.model.PlayerModel;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.KoboldGeomancer;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.card.spellcard.concrete.HolySmite;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.card.spellcard.concrete.WildGrowth;
import com.hearthsim.card.weapon.concrete.FieryWarAxe;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestHeroBase {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    private Deck deck;

    @Before
    public void setup() throws HSException {
        TestHero self = new TestHero();
        self.enableHeroAbility = true;
        board = new HearthTreeNode(new BoardModel(self, new TestHero()));
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

        currentPlayer.getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER,
                currentPlayer.getHero(), board, deck, null);
        currentPlayer.getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER,
                currentPlayer.getHero(), board, deck, null);

        board.data_.resetMana();
        board.data_.resetMinions();
    }

    @Test
    public void testHeroCannotAttack() throws HSException {
        Minion target = waitingPlayer.getCharacter(0);
        Hero hero = currentPlayer.getHero();

        assertFalse(hero.canAttack());

        HearthTreeNode ret = hero.attack(PlayerSide.WAITING_PLAYER, target, board, deck, null, false);
        assertNull(ret);
    }

    @Test
    public void testMinionAttackingHero() throws HSException {

        // null case
        Minion target = waitingPlayer.getCharacter(0);
        Minion minion = currentPlayer.getMinions().get(0);
        HearthTreeNode ret = minion.attack(PlayerSide.WAITING_PLAYER, target, board, deck, null, false);
        assertEquals(board, ret);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 2);

        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 28);

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
    public void testHeropowerIsMarkedUsed() throws HSException {
        Hero hero = currentPlayer.getHero();
        Minion target = currentPlayer.getCharacter(0); // Hero
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
        assertEquals(board, ret);
        assertTrue(hero.hasBeenUsed());
    }

    @Test
    public void testHeropowerSubtractsMana() throws HSException {
        Hero hero = currentPlayer.getHero();
        Minion target = currentPlayer.getCharacter(0); // Hero
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
        assertEquals(board, ret);
        assertEquals(currentPlayer.getMana(), 6);
    }

    @Test
    public void testHeropowerCannotBeUsedTwice() throws HSException {
        Hero hero = currentPlayer.getHero();
        Minion target = currentPlayer.getCharacter(0); // Hero
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
        assertEquals(board, ret);

        target = currentPlayer.getCharacter(0); // Hero
        assertFalse(hero.canBeUsedOn(PlayerSide.CURRENT_PLAYER, target, board.data_));

        ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
        assertNull(ret);

        assertTrue(hero.hasBeenUsed());
        assertEquals(currentPlayer.getMana(), 6);
    }

    @Test
    public void testHeropowerResets() throws HSException {
        Hero hero = currentPlayer.getHero();
        Minion target = currentPlayer.getCharacter(0); // Hero
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
        assertEquals(board, ret);

        board.data_.resetMinions();
        assertFalse(hero.hasBeenUsed());
    }

    @Test
    public void testNotEnoughMana() throws HSException {
        Hero hero = currentPlayer.getHero();
        currentPlayer.setMana((byte) 1);

        Minion target = currentPlayer.getCharacter(0); // Hero
        //assertFalse(hero.canBeUsedOn(PlayerSide.CURRENT_PLAYER, target, board.data_)); // TODO doesn't work yet

        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
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

        Minion minion = currentPlayer.getMinions().get(0);
        HearthTreeNode ret = minion.attack(PlayerSide.WAITING_PLAYER, opponent, board, deck, null, false);
        assertEquals(board, ret);

        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getArmor(), 1);
    }

    @Test
    @Ignore("Existing bug")
    public void testSpellpowerEffectsArmor() throws HSException {
        Hero opponent = waitingPlayer.getHero();
        opponent.setArmor((byte)10);

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new KoboldGeomancer());

        HolySmite smite = new HolySmite();
        board.data_.placeCardHand(PlayerSide.CURRENT_PLAYER, smite);

        HearthTreeNode ret = smite.useOn(PlayerSide.WAITING_PLAYER, opponent, board, deck, null);
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
