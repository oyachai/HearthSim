package com.hearthsim.test;

import com.hearthsim.card.basic.minion.BloodfenRaptor;
import com.hearthsim.card.minion.Minion;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestMinionResetCopy {

    private BloodfenRaptor original;
    private BloodfenRaptor copy;

    @Before
    public void setUp() throws Exception {
        original = new BloodfenRaptor();
        copy = new BloodfenRaptor();
    }

    @Test
    public void testResetAttack() {
        copy.setAttack((byte)20);
        Minion newCopy = (Minion)copy.createResetCopy();
        assertEquals(original, newCopy);
    }

    @Test
    public void testResetDivineShield() {
        copy.setDivineShield(true);
        Minion newCopy = (Minion)copy.createResetCopy();
        assertEquals(original, newCopy);
    }

    @Test
    public void testResetHealth() {
        copy.setHealth((byte)20);
        Minion newCopy = (Minion)copy.createResetCopy();
        assertEquals(original, newCopy);
    }

    @Test
    public void testResetSpellDamage() {
        copy.setSpellDamage((byte)20);
        Minion newCopy = (Minion)copy.createResetCopy();
        assertEquals(original, newCopy);
    }

    @Test
    public void testResetWindfury() {
        copy.setWindfury(true);
        Minion newCopy = (Minion)copy.createResetCopy();
        assertEquals(original, newCopy);
    }
}
