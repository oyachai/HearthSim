package com.hearthsim.test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.basic.minion.BloodfenRaptor;
import com.hearthsim.card.basic.spell.HolySmite;
import com.hearthsim.card.basic.spell.TheCoin;
import com.hearthsim.card.minion.heroes.Priest;
import com.hearthsim.card.minion.heroes.Warlock;
import com.hearthsim.model.PlayerModel;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestPlayerModel {

    private static final int nT = 100;
    private static final List<Card> CARD_LIST = Arrays.asList(new HolySmite(), new BloodfenRaptor(), new TheCoin(),
            new HolySmite());

    private Deck deck0;
    private Deck deck1;

    @Before
    public void setup() {

        Card cards[] = new Card[10];
        for (int index = 0; index < 10; ++index) {
            cards[index] = new TheCoin();
        }

        deck0 = new Deck(cards);
        deck1 = new Deck(cards);
    }

    @Test
    public void testEquals() {
        PlayerModel player0 = new PlayerModel((byte)0, "player0", new Priest(), deck0);
        PlayerModel player1 = new PlayerModel((byte)0, "player0", new Priest(), deck1);

        assertEquals(player0, player1);
        assertEquals(player1, player0);
    }

    @Test
    public void testEqualDecksRandom() {
        int numCards = 2;
        Card[] cards0 = new Card[numCards];
        Card[] cards1 = new Card[numCards];

        for (int iter = 0; iter < nT; ++iter) {

            for (int i = 0; i < numCards; ++i) {
                int indx0 = (int)(Math.random() * CARD_LIST.size());
                int indx1 = (int)(Math.random() * CARD_LIST.size());
                cards0[i] = CARD_LIST.get(indx0);
                cards1[i] = CARD_LIST.get(indx1);
            }

            Deck deckRandom0 = new Deck(cards0);
            Deck deckRandom1 = new Deck(cards1);

            PlayerModel player0 = new PlayerModel((byte)0, "player0", new Priest(), deckRandom0);
            PlayerModel player1 = new PlayerModel((byte)0, "player0", new Priest(), deckRandom1);

            boolean deckIsSame = true;
            for (int i = 0; i < numCards; ++i) {
                deckIsSame = deckIsSame && cards0[i].equals(cards1[i]);
            }
            if (deckIsSame) {
                assertEquals(player0, player1);
                assertEquals(player1, player0);
                assertEquals(player0.hashCode(), player1.hashCode());
            } else {
                assertNotEquals(player0, player1);
                assertNotEquals(player1, player0);
                assertNotEquals(player0.hashCode(), player1.hashCode());
            }
        }
    }

    @Test
    public void testEqualRandomHealth() {
        PlayerModel player0 = new PlayerModel((byte)0, "player0", new Priest(), deck0);
        PlayerModel player1 = new PlayerModel((byte)0, "player0", new Priest(), deck1);

        for (int iter = 0; iter < nT; ++iter) {
            byte health0 = (byte)(Math.random() * 10 + 20.0);
            byte health1 = (byte)(Math.random() * 10 + 20.0);

            player0.getHero().setHealth(health0);
            player1.getHero().setHealth(health1);

            if (health0 == health1) {
                assertEquals(player0, player1);
                assertEquals(player1, player0);
                assertEquals(player0.hashCode(), player1.hashCode());
            } else {
                assertNotEquals(player0, player1);
                assertNotEquals(player1, player0);
                assertNotEquals(player0.hashCode(), player1.hashCode());
            }
        }
    }

    @Test
    public void testEqualRandomArmor() {
        PlayerModel player0 = new PlayerModel((byte)0, "player0", new Priest(), deck0);
        PlayerModel player1 = new PlayerModel((byte)0, "player0", new Priest(), deck1);

        for (int iter = 0; iter < nT; ++iter) {
            byte armor0 = (byte)(Math.random() * 10);
            byte armor1 = (byte)(Math.random() * 10);

            player0.getHero().setArmor(armor0);
            player1.getHero().setArmor(armor1);

            if (armor0 == armor1) {
                assertEquals(player0, player1);
                assertEquals(player1, player0);
                assertEquals(player0.hashCode(), player1.hashCode());
            } else {
                assertNotEquals(player0, player1);
                assertNotEquals(player1, player0);
                assertNotEquals(player0.hashCode(), player1.hashCode());
            }
        }
    }

    @Test
    public void testNotEqualHero() {
        PlayerModel player0 = new PlayerModel((byte)0, "player0", new Priest(), deck0);
        PlayerModel player1 = new PlayerModel((byte)0, "player0", new Warlock(), deck1);

        assertNotEquals(player0, player1);
        assertNotEquals(player1, player0);
    }

    @Test
    public void testNotEqualName() {
        PlayerModel player0 = new PlayerModel((byte)0, "player0", new Priest(), deck0);
        PlayerModel player1 = new PlayerModel((byte)0, "player1", new Priest(), deck1);

        assertNotEquals(player0, player1);
        assertNotEquals(player1, player0);
    }

    @Test
    public void testNotEqualPlayerId() {
        PlayerModel player0 = new PlayerModel((byte)0, "player0", new Priest(), deck0);
        PlayerModel player1 = new PlayerModel((byte)1, "player0", new Priest(), deck1);

        assertNotEquals(player0, player1);
        assertNotEquals(player1, player0);
    }
}
