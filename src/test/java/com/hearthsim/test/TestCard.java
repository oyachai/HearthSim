package com.hearthsim.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import com.hearthsim.card.Card;

public class TestCard {
    @Test
    public void testEqualsDeepCopy() {
        Card card0 = new Card("" + 0, (byte)2, false, false, (byte)0);
        Card copy = card0.deepCopy();
        assertEquals(card0, copy);

        copy.setBaseManaCost((byte)3);
        assertNotEquals(card0, copy);
    }

    @Test
    public void testEqualsSelf() {
        Card card0 = new Card("" + 0, (byte)2, false, false, (byte)0);
        assertEquals(card0, card0);
    }

    @Test
    public void testNotEqualsHasBeenUsed() {
        Card card0 = new Card("" + 0, (byte)2, true, true, (byte)0);
        Card card1 = new Card("" + 0, (byte)2, false, true, (byte)0);
        assertNotEquals(card0, card1);
    }

    @Test
    public void testNotEqualsIsInHand() {
        Card card0 = new Card("" + 0, (byte)2, true, true, (byte)0);
        Card card1 = new Card("" + 0, (byte)2, true, false, (byte)0);
        assertNotEquals(card0, card1);
    }

    @Test
    public void testNotEqualsMana() {
        Card card0 = new Card("" + 0, (byte)2, false, false, (byte)0);
        Card card1 = new Card("" + 0, (byte)3, false, false, (byte)0);
        assertNotEquals(card0, card1);
    }

    @Test
    public void testNotEqualsName() {
        Card card0 = new Card("" + 0, (byte)2, false, false, (byte)0);
        Card card1 = new Card("" + 1, (byte)2, false, false, (byte)0);
        assertNotEquals(card0, card1);
    }
}
