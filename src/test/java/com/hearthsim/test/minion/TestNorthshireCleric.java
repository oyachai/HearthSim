package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.Deck;
import com.hearthsim.card.basic.minion.NorthshireCleric;
import com.hearthsim.card.basic.spell.AncestralHealing;
import com.hearthsim.card.basic.spell.TheCoin;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionMock;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestNorthshireCleric {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    private static final byte mana = 2;
    private static final byte attack0 = 2;
    private static final byte health0 = 3;
    private static final byte health1 = 7;

    @Before
    public void setup() throws HSException {
        Card cards[] = new Card[10];
        for (int index = 0; index < 10; ++index) {
            cards[index] = new TheCoin();
        }

        Deck deck = new Deck(cards);
        board = new HearthTreeNode(new BoardModel(deck, deck));
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        Minion minion0_0 = new MinionMock("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion0_1 = new MinionMock("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);
        Minion minion1_0 = new MinionMock("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion1_1 = new MinionMock("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_1);

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_1);

        currentPlayer.setMana((byte) 10);
        waitingPlayer.setMana((byte) 10);
    }

    @Test
    public void test_deepCopy() {

        Minion card1 = new NorthshireCleric();
        Minion card1_cloned = (Minion)card1.deepCopy();

        assertTrue(card1.equals(card1_cloned));
        assertTrue(card1_cloned.equals(card1));

        card1.setHealth((byte)(card1.getHealth() + 1));
        assertFalse(card1.equals(card1_cloned));
        assertFalse(card1_cloned.equals(card1));

        card1_cloned = (Minion)card1.deepCopy();
        assertTrue(card1.equals(card1_cloned));
        assertTrue(card1_cloned.equals(card1));

        card1.setAttack((byte)(card1.getTotalAttack() + 1));
        assertFalse(card1.equals(card1_cloned));
        assertFalse(card1_cloned.equals(card1));

        card1_cloned = (Minion)card1.deepCopy();
        assertTrue(card1.equals(card1_cloned));
        assertTrue(card1_cloned.equals(card1));

        card1.setDestroyOnTurnEnd(true);
        assertFalse(card1.equals(card1_cloned));
        assertFalse(card1_cloned.equals(card1));
        card1_cloned = (Minion)card1.deepCopy();
        assertTrue(card1.equals(card1_cloned));
        assertTrue(card1_cloned.equals(card1));

        card1.setDestroyOnTurnStart(true);
        assertFalse(card1.equals(card1_cloned));
        assertFalse(card1_cloned.equals(card1));
        card1_cloned = (Minion)card1.deepCopy();
        assertTrue(card1.equals(card1_cloned));
        assertTrue(card1_cloned.equals(card1));
    }

    @Test
    public void test1() throws HSException {
        NorthshireCleric fb = new NorthshireCleric();
        currentPlayer.placeCardHand(fb);

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.HERO, board);
        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health0);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getHealth(), health1 - 1);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health1 - 1);

        AncestralHealing ah = new AncestralHealing();
        currentPlayer.placeCardHand(ah);
        theCard = currentPlayer.getHand().get(0);
        ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1, board);

        assertFalse(ret == null);
        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health0);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getHealth(), health1 - 1);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health1 - 1);

        ah = new AncestralHealing();
        currentPlayer.placeCardHand(ah);
        theCard = currentPlayer.getHand().get(0);
        ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_3, board);

        assertFalse(ret == null);
        assertEquals(currentPlayer.getHand().size(), 0);
        assertTrue(ret instanceof CardDrawNode);
        assertEquals( ((CardDrawNode)ret).getNumCardsToDraw(), 1); //Northshire Cleric should have drawn a card, so 1 card now
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health0);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getHealth(), health1);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health1 - 1);
    }

    @Test
    public void test2() throws HSException {
        NorthshireCleric fb1 = new NorthshireCleric();
        NorthshireCleric fb2 = new NorthshireCleric();

        currentPlayer.placeCardHand(fb1);
        currentPlayer.placeCardHand(fb2);

        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret;
        theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_2, board);

        theCard = currentPlayer.getHand().get(0);
        ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_3, board);
        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 4);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health1 - 1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getHealth(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_4).getHealth(), 3);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health1 - 1);

        AncestralHealing ah = new AncestralHealing();
        currentPlayer.placeCardHand(ah);
        theCard = currentPlayer.getHand().get(0);
        ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_2, board);

        assertFalse(ret == null);
        assertEquals(currentPlayer.getHand().size(), 0);
        assertTrue(ret instanceof CardDrawNode);
        assertEquals( ((CardDrawNode)ret).getNumCardsToDraw(), 2); //Two clerics, one heal means 2 new cards

        assertEquals(currentPlayer.getNumMinions(), 4);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health1);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_3).getHealth(), 3);
        assertEquals(currentPlayer.getCharacter(CharacterIndex.MINION_4).getHealth(), 3);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_1).getHealth(), health0);
        assertEquals(waitingPlayer.getCharacter(CharacterIndex.MINION_2).getHealth(), health1 - 1);
    }
}
