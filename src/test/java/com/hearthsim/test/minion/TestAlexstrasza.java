package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.Alexstrasza;
import com.hearthsim.card.minion.concrete.ArgentSquire;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestAlexstrasza {

    private HearthTreeNode board;
    private Deck deck;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());

        Minion minion0_0 = new BoulderfistOgre();
        Minion minion0_1 = new RaidLeader();
        Minion minion1_0 = new BoulderfistOgre();
        Minion minion1_1 = new RaidLeader();
        Minion minion1_2 = new ArgentSquire();

        board.data_.getCurrentPlayer().placeCardHand(minion0_0);
        board.data_.getCurrentPlayer().placeCardHand(minion0_1);

        board.data_.getWaitingPlayer().placeCardHand(minion1_0);
        board.data_.getWaitingPlayer().placeCardHand(minion1_1);
        board.data_.getWaitingPlayer().placeCardHand(minion1_2);

        Card cards[] = new Card[10];
        for (int index = 0; index < 10; ++index) {
            cards[index] = new TheCoin();
        }

        deck = new Deck(cards);

        Card fb = new Alexstrasza();
        board.data_.getCurrentPlayer().placeCardHand(fb);

        board.data_.getCurrentPlayer().setMana((byte)10);
        board.data_.getWaitingPlayer().setMana((byte)10);

        board.data_.getCurrentPlayer().setMaxMana((byte)9);
        board.data_.getWaitingPlayer().setMaxMana((byte)9);

        HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
        tmpBoard.data_.getCard_hand(PlayerSide.CURRENT_PLAYER, 0).useOn(PlayerSide.CURRENT_PLAYER,
                tmpBoard.data_.getCurrentPlayer().getHero(), tmpBoard, deck, null);
        tmpBoard.data_.getCard_hand(PlayerSide.CURRENT_PLAYER, 0).useOn(PlayerSide.CURRENT_PLAYER,
                tmpBoard.data_.getCurrentPlayer().getHero(), tmpBoard, deck, null);
        tmpBoard.data_.getCard_hand(PlayerSide.CURRENT_PLAYER, 0).useOn(PlayerSide.CURRENT_PLAYER,
                tmpBoard.data_.getCurrentPlayer().getHero(), tmpBoard, deck, null);

        board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
        board.data_.getCard_hand(PlayerSide.CURRENT_PLAYER, 0).useOn(PlayerSide.CURRENT_PLAYER,
                board.data_.getCurrentPlayer().getHero(), board, deck, null);
        board.data_.getCard_hand(PlayerSide.CURRENT_PLAYER, 0).useOn(PlayerSide.CURRENT_PLAYER,
                board.data_.getCurrentPlayer().getHero(), board, deck, null);

        board.data_.resetMana();
        board.data_.resetMinions();

    }

    @Test
    public void testSetsOwnHealth() throws HSException {
        Minion hero = board.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getCharacter(0);
        Alexstrasza alexstrasza = new Alexstrasza();
        alexstrasza.useTargetableBattlecry_core(PlayerSide.CURRENT_PLAYER, hero, board, null, null);
        assertEquals(15, hero.getHealth());
    }

    @Test
    public void testSetsEnemyHealth() throws HSException {
        Minion hero = board.data_.modelForSide(PlayerSide.WAITING_PLAYER).getCharacter(0);
        Alexstrasza alexstrasza = new Alexstrasza();
        alexstrasza.useTargetableBattlecry_core(PlayerSide.WAITING_PLAYER, hero, board, null, null);
        assertEquals(15, hero.getHealth());
    }

    @Test
    public void testSetsDamagedHealth() throws HSException {
        Minion hero = board.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getCharacter(0);
        hero.setHealth((byte)20);
        Alexstrasza alexstrasza = new Alexstrasza();
        alexstrasza.useTargetableBattlecry_core(PlayerSide.CURRENT_PLAYER, hero, board, null, null);
        assertEquals(15, hero.getHealth());
    }

    @Test
    public void testDoesNotEffectArmor() throws HSException {
        Hero hero = board.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getHero();
        hero.setArmor((byte)20);
        Alexstrasza alexstrasza = new Alexstrasza();
        alexstrasza.useTargetableBattlecry_core(PlayerSide.CURRENT_PLAYER, hero, board, null, null);
        assertEquals(15, hero.getHealth());
        assertEquals(20, hero.getArmor());
    }

    @Test
    // TODO check to see if this counts as an actual heal effect (e.g., Soulpriest or Lightwarden)
    public void testHealsLowHealthTarget() throws HSException {
        Minion hero = board.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getCharacter(0);
        hero.setHealth((byte)2);
        Alexstrasza alexstrasza = new Alexstrasza();
        alexstrasza.useTargetableBattlecry_core(PlayerSide.CURRENT_PLAYER, hero, board, null, null);
        assertEquals(15, hero.getHealth());
    }
}
