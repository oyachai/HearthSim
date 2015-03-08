package com.hearthsim.test.heroes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.hearthsim.model.PlayerModel;
import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.minion.heroes.Druid;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestDruid {

    private HearthTreeNode board;
    private Deck deck;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel(new Druid(), new TestHero()));

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

        board.data_.getCurrentPlayer().setMana((byte)9);
        board.data_.getWaitingPlayer().setMana((byte)9);

        board.data_.getCurrentPlayer().setMaxMana((byte)8);
        board.data_.getWaitingPlayer().setMaxMana((byte)8);

        HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
        tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER,
                tmpBoard.data_.getCurrentPlayer().getHero(), tmpBoard, deck, null);
        tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER,
                tmpBoard.data_.getCurrentPlayer().getHero(), tmpBoard, deck, null);

        board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
        board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER,
                board.data_.getCurrentPlayer().getHero(), board, deck, null);
        board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER,
                board.data_.getCurrentPlayer().getHero(), board, deck, null);

        board.data_.resetMana();
        board.data_.resetMinions();
    }

    @Test
    public void testHeropower() throws HSException {
        Hero hero = board.data_.getCurrentPlayer().getHero();
        Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0); // Druid hero
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
        assertEquals(board, ret);

        assertEquals(board.data_.getNumCards_hand(), 0);
    }

    @Test
    public void testArmorIsAdditive() throws HSException {
        Hero hero = board.data_.getCurrentPlayer().getHero();
        hero.setArmor((byte)1);

        Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0); // Druid hero
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
        assertEquals(board, ret);
        assertEquals(board.data_.getCurrentPlayer().getHero().getArmor(), 2);
    }

    @Test
    public void testAttackIsAdditive() throws HSException {
        Hero hero = board.data_.getCurrentPlayer().getHero();
        hero.setExtraAttackUntilTurnEnd((byte)1);

        Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0); // Druid hero
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, target, board, deck, null);
        assertEquals(board, ret);
        assertEquals(board.data_.getCurrentPlayer().getHero().getExtraAttackUntilTurnEnd(), 2);
    }

    @Test
    public void testCannotTargetMinion() throws HSException {
        Minion raidLeader = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 1); // Raid leader
        Hero hero = board.data_.getCurrentPlayer().getHero();
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.CURRENT_PLAYER, raidLeader, board, deck, null);
        assertNull(ret);
        PlayerModel currentPlayer = board.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = board.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        assertEquals(board.data_.getCurrentPlayer().getMana(), 8);
        assertEquals(board.data_.getCurrentPlayer().getHero().getArmor(), 0);
        assertEquals(board.data_.getCurrentPlayer().getHero().getTotalAttack(), 0);
        assertEquals(board.data_.getCurrentPlayer().getHero().getExtraAttackUntilTurnEnd(), 0);

        assertEquals(currentPlayer.getMinions().get(0).getAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 2);
    }

    @Test
    public void testCannotTargetOpponent() throws HSException {
        Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 0);
        Hero hero = board.data_.getCurrentPlayer().getHero();
        HearthTreeNode ret = hero.useHeroAbility(PlayerSide.WAITING_PLAYER, target, board, deck, null);
        assertNull(ret);

        assertEquals(board.data_.getNumCards_hand(), 0);
    }
}
