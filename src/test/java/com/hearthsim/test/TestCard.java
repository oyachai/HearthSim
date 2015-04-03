package com.hearthsim.test;

import com.hearthsim.card.Card;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestCard {
    @Test
    public void testEqualsDeepCopy() {
        Card card0 = new Card();
        card0.setBaseManaCost((byte)1);
        Card copy = card0.deepCopy();
        assertEquals(card0, copy);

        copy.setBaseManaCost((byte)3);
        assertNotEquals(card0, copy);
    }

    @Test
    public void testEqualsSelf() {
        Card card0 = new Card();
        card0.setBaseManaCost((byte)1);

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
        card0.isInHand(false);
        Card card1 = new Card();
        assertNotEquals(card0, card1);
    }

    @Test
    public void testNotEqualsMana() {
        Card card0 = new Card();
        card0.setBaseManaCost((byte) 5);
        Card card1 = new Card();
        assertNotEquals(card0, card1);
    }

    @Test
    public void testNotEqualsName() {
        Card card0 = new Card();
        card0.setName("Test");
        Card card1 = new Card();
        assertNotEquals(card0, card1);
    }
}
