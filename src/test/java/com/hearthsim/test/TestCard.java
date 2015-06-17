package com.hearthsim.test;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestCard {
    @Test
    public void testEqualsDeepCopy() {
        Card card0 = new Card();
        card0.hasBeenUsed(true);
        Card copy = card0.deepCopy();
        assertEquals(card0, copy);

        card0.hasBeenUsed(false);
        assertNotEquals(card0, copy);
    }

    @Test
    public void testEqualsSelf() {
        Card card0 = new Card();
        card0.hasBeenUsed(true);

        assertEquals(card0, card0);
    }

    @Test
    public void testNotEqualsHasBeenUsed() {
        Card card0 = new Card();
        card0.hasBeenUsed(true);
        Card card1 = new Card();
        assertNotEquals(card0, card1);
    }

    @Test
    public void testNotEqualsIsInHand() {
        Card card0 = new Card();
        card0.setInHand(false);
        Card card1 = new Card();
        assertNotEquals(card0, card1);
    }

    @Test
    public void testNotEqualsMana() {
        CardMock card0 = new CardMock();
        card0.setBaseManaCost((byte) 5);
        CardMock card1 = new CardMock();
        assertNotEquals(card0, card1);
    }

    @Test
    public void testNotEqualsName() {
        CardMock card0 = new CardMock();
        card0.setName("Test");
        CardMock card1 = new CardMock();
        assertNotEquals(card0, card1);
    }
}
