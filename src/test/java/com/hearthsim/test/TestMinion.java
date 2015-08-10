package com.hearthsim.test;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionMock;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestMinion {
    private static final int nT = 100;

    @Test
    public void testAttackDeepCopy() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        Minion copy = (Minion)minion0.deepCopy();
        assertEquals(minion0, copy);
        assertEquals(copy, minion0);
        assertEquals(minion0.hashCode(), copy.hashCode());

        // Verify the copy is a different object
        copy.setAttack((byte)10);
        assertEquals(10, copy.getAttack());
        assertEquals(3, minion0.getAttack());
    }

    @Test
    public void testAttackNotEquals() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        Minion minion1 = new MinionMock("" + 0, (byte)2, (byte)30, (byte)4, (byte)3, (byte)4, (byte)4);
        assertNotEquals(minion0, minion1);
        assertNotEquals(minion0.hashCode(), minion1.hashCode());
    }

    @Test
    public void testAuraAttackDeepCopy() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        minion0.setAuraAttack((byte)3);

        Minion copy = (Minion)minion0.deepCopy();
        assertEquals(minion0, copy);
        assertEquals(copy, minion0);
        assertEquals(minion0.hashCode(), copy.hashCode());

        // Verify the copy is a different object
        copy.setAuraAttack((byte)10);
        assertEquals(10, copy.getAuraAttack());
        assertEquals(3, minion0.getAuraAttack());
    }

    @Test
    public void testAuraAttackNotEquals() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        Minion copy = (Minion)minion0.deepCopy();
        copy.setAuraAttack((byte)3);
        assertNotEquals(minion0, copy);
        assertNotEquals(minion0.hashCode(), copy.hashCode());
    }

    @Test
    public void testAuraHealthDeepCopy() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        minion0.setAuraHealth((byte)3);

        Minion copy = (Minion)minion0.deepCopy();
        assertEquals(minion0, copy);
        assertEquals(copy, minion0);
        assertEquals(minion0.hashCode(), copy.hashCode());

        // Verify the copy is a different object
        copy.setAuraHealth((byte)10);
        assertEquals(10, copy.getAuraHealth());
        assertEquals(3, minion0.getAuraHealth());
    }

    @Test
    public void testAuraHealthNotEquals() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        Minion copy = (Minion)minion0.deepCopy();
        copy.setAuraHealth((byte)3);
        assertNotEquals(minion0, copy);
        assertNotEquals(minion0.hashCode(), copy.hashCode());
    }

    @Test
    public void testChargeDeepCopy() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        minion0.setCharge(true);

        Minion copy = (Minion)minion0.deepCopy();
        assertEquals(minion0, copy);
        assertEquals(copy, minion0);
        assertEquals(minion0.hashCode(), copy.hashCode());

        // Verify the copy is a different object
        copy.setCharge(false);
        assertFalse(copy.getCharge());
        assertTrue(minion0.getCharge());
    }

    @Test
    public void testChargeNotEquals() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        Minion copy = (Minion)minion0.deepCopy();
        copy.setCharge(true);
        assertNotEquals(minion0, copy);
        assertNotEquals(minion0.hashCode(), copy.hashCode());
    }

    @Test
    public void testDestroyOnTurnEndNotEquals() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        Minion minion1 = (Minion)minion0.deepCopy();
        minion1.setDestroyOnTurnEnd(true);
        assertNotEquals(minion0, minion1);
        assertNotEquals(minion0.hashCode(), minion1.hashCode());
    }

    @Test
    public void testDestroyOnTurnStartNotEquals() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        Minion minion1 = (Minion)minion0.deepCopy();
        minion1.setDestroyOnTurnStart(true);
        assertNotEquals(minion0, minion1);
        assertNotEquals(minion0.hashCode(), minion1.hashCode());
    }

    @Test
    public void testDivineShieldDeepCopy() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        minion0.setDivineShield(true);

        Minion copy = (Minion)minion0.deepCopy();
        assertEquals(minion0, copy);
        assertEquals(copy, minion0);
        assertEquals(minion0.hashCode(), copy.hashCode());

        // Verify the copy is a different object
        copy.setDivineShield(false);
        assertFalse(copy.getDivineShield());
        assertTrue(minion0.getDivineShield());
    }

    @Test
    public void testDivineShieldNotEquals() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        Minion copy = (Minion)minion0.deepCopy();
        copy.setDivineShield(true);
        assertNotEquals(minion0, copy);
        assertNotEquals(minion0.hashCode(), copy.hashCode());
    }

    @Test
    public void testEquals() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        Minion minion1 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        assertEquals(minion0, minion1);
        assertEquals(minion0.hashCode(), minion1.hashCode());
    }

    @Test
    public void testEqualsRandom() {

        for (int iter = 0; iter < nT; ++iter) {
            byte mana = (byte)(Math.random() * 10);
            byte attack = (byte)(Math.random() * 10);
            byte health = (byte)(Math.random() * 10);
            Minion minion1 = new MinionMock("minion1", mana, attack, health, attack, health, health);
            Minion minion2 = new MinionMock("minion1", mana, attack, health, attack, health, health);
            assertEquals(minion1, minion2);
            assertEquals(minion2, minion1);
            assertEquals(minion1.hashCode(), minion2.hashCode());
        }
    }

    @Test
    public void testEqualsSelf() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        assertEquals(minion0, minion0);
    }

    @Test
    public void testFrozenDeepCopy() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        minion0.setFrozen(true);

        Minion copy = (Minion)minion0.deepCopy();
        assertEquals(minion0, copy);
        assertEquals(copy, minion0);
        assertEquals(minion0.hashCode(), copy.hashCode());

        // Verify the copy is a different object
        copy.setFrozen(false);
        assertFalse(copy.getFrozen());
        assertTrue(minion0.getFrozen());
    }

    @Test
    public void testFrozenNotEquals() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        Minion copy = (Minion)minion0.deepCopy();
        copy.setFrozen(true);
        assertNotEquals(minion0, copy);
        assertNotEquals(minion0.hashCode(), copy.hashCode());
    }

    @Test
    public void testHasWindfuryAttackedDeepCopy() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        minion0.hasWindFuryAttacked(true);

        Minion copy = (Minion)minion0.deepCopy();
        assertEquals(minion0, copy);
        assertEquals(copy, minion0);
        assertEquals(minion0.hashCode(), copy.hashCode());

        // Verify the copy is a different object
        copy.hasWindFuryAttacked(false);
        assertFalse(copy.hasWindFuryAttacked());
        assertTrue(minion0.hasWindFuryAttacked());
    }

    @Test
    public void testHasWindfuryAttackedNotEquals() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        Minion copy = (Minion)minion0.deepCopy();
        copy.hasWindFuryAttacked(true);
        assertNotEquals(minion0, copy);
        assertNotEquals(minion0.hashCode(), copy.hashCode());
    }

    @Test
    public void testHealthNotEquals() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        Minion minion1 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)40, (byte)3, (byte)4, (byte)4);
        assertNotEquals(minion0, minion1);
        assertNotEquals(minion0.hashCode(), minion1.hashCode());
    }

    @Test
    public void testHeroTargetableDeepCopy() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        minion0.setHeroTargetable(false);

        Minion copy = (Minion)minion0.deepCopy();
        assertEquals(minion0, copy);
        assertEquals(copy, minion0);
        assertEquals(minion0.hashCode(), copy.hashCode());

        // Verify the copy is a different object
        copy.setHeroTargetable(true);
        assertTrue(copy.isHeroTargetable());
        assertFalse(minion0.isHeroTargetable());
    }

    @Test
    public void testHeroTargetableNotEquals() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        Minion copy = (Minion)minion0.deepCopy();
        copy.setHeroTargetable(false);
        assertNotEquals(minion0, copy);
        assertNotEquals(minion0.hashCode(), copy.hashCode());
    }

    @Test
    public void testManaNotEquals() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        Minion minion1 = new MinionMock("" + 0, (byte)20, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        assertNotEquals(minion0, minion1);
        assertNotEquals(minion0.hashCode(), minion1.hashCode());
    }

    @Test
    public void testMaxHealthNotEquals() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        Minion minion1 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)40);
        assertNotEquals(minion0, minion1);
        assertNotEquals(minion0.hashCode(), minion1.hashCode());
    }

    @Test
    public void testNameNotEquals() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        Minion minion1 = new MinionMock("" + 1, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        assertNotEquals(minion0, minion1);
        assertNotEquals(minion0.hashCode(), minion1.hashCode());
    }

    @Test
    public void testSpellDamageDeepCopy() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        minion0.setSpellDamage((byte)3);

        Minion copy = (Minion)minion0.deepCopy();
        assertEquals(minion0, copy);
        assertEquals(copy, minion0);
        assertEquals(minion0.hashCode(), copy.hashCode());

        // Verify the copy is a different object
        copy.setSpellDamage((byte)10);
        assertEquals(10, copy.getSpellDamage());
        assertEquals(3, minion0.getSpellDamage());
    }

    @Test
    public void testSpellDamageNotEquals() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        Minion copy = (Minion)minion0.deepCopy();
        copy.setSpellDamage((byte)3);
        assertNotEquals(minion0, copy);
        assertNotEquals(minion0.hashCode(), copy.hashCode());
    }

    @Test
    public void testStealthedDeepCopy() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        minion0.setStealthedUntilRevealed(true);

        Minion copy = (Minion)minion0.deepCopy();
        assertEquals(minion0, copy);
        assertEquals(copy, minion0);
        assertEquals(minion0.hashCode(), copy.hashCode());

        // Verify the copy is a different object
        copy.setStealthedUntilRevealed(false);
        assertFalse(copy.isStealthed());
        assertTrue(minion0.isStealthed());
    }

    @Test
    public void testStealthedNotEquals() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        Minion copy = (Minion)minion0.deepCopy();
        copy.setStealthedUntilRevealed(true);
        assertNotEquals(minion0, copy);
        assertNotEquals(minion0.hashCode(), copy.hashCode());
    }

    @Test
    public void testTauntDeepCopy() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        minion0.setTaunt(true);

        Minion copy = (Minion)minion0.deepCopy();
        assertEquals(minion0, copy);
        assertEquals(copy, minion0);
        assertEquals(minion0.hashCode(), copy.hashCode());

        // Verify the copy is a different object
        copy.setTaunt(false);
        assertFalse(copy.getTaunt());
        assertTrue(minion0.getTaunt());
    }

    @Test
    public void testTauntNotEquals() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        Minion copy = (Minion)minion0.deepCopy();
        copy.setTaunt(true);
        assertNotEquals(minion0, copy);
        assertNotEquals(minion0.hashCode(), copy.hashCode());
    }

    @Test
    public void testWindfuryDeepCopy() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        minion0.setWindfury(true);

        Minion copy = (Minion)minion0.deepCopy();
        assertEquals(minion0, copy);
        assertEquals(copy, minion0);
        assertEquals(minion0.hashCode(), copy.hashCode());

        // Verify the copy is a different object
        copy.setWindfury(false);
        assertFalse(copy.getWindfury());
        assertTrue(minion0.getWindfury());
    }

    @Test
    public void testWindfuryNotEquals() {
        Minion minion0 = new MinionMock("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
        Minion copy = (Minion)minion0.deepCopy();
        copy.setWindfury(true);
        assertNotEquals(minion0, copy);
        assertNotEquals(minion0.hashCode(), copy.hashCode());
    }
}
