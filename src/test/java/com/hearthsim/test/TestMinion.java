package com.hearthsim.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import com.hearthsim.card.minion.Minion;

public class TestMinion {
	private static final int nT = 100;

	@Test
	public void testEqualsDeepCopy() {
		Minion minion0 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		Minion copy = (Minion)minion0.deepCopy();
		assertEquals(minion0, copy);
		assertEquals(copy, minion0);
		assertEquals(minion0.hashCode(), copy.hashCode());

		copy.setAttack((byte)10);
		assertNotEquals(minion0, copy);
	}

	@Test
	public void testEqualsDeepCopySpellpower() {
		Minion minion0 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		minion0.setSpellDamage((byte)3);

		Minion copy = (Minion)minion0.deepCopy();
		assertEquals(minion0, copy);
		assertEquals(copy, minion0);
		assertEquals(minion0.hashCode(), copy.hashCode());

		copy.setSpellDamage((byte)10);
		assertNotEquals(minion0, copy);
	}

	@Test
	public void testEquals() {
		Minion minion0 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		Minion minion1 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		assertEquals(minion0, minion1);
		assertEquals(minion0.hashCode(), minion1.hashCode());
	}

	@Test
	public void testEqualsRandom() {

		for(int iter = 0; iter < nT; ++iter) {
			byte mana = (byte)(Math.random() * 10);
			byte attack = (byte)(Math.random() * 10);
			byte health = (byte)(Math.random() * 10);
			Minion minion1 = new Minion("minion1", mana, attack, health, attack, health, health);
			Minion minion2 = new Minion("minion1", mana, attack, health, attack, health, health);
			assertEquals(minion1, minion2);
			assertEquals(minion2, minion1);
			assertEquals(minion1.hashCode(), minion2.hashCode());
		}
	}

	@Test
	public void testEqualsSelf() {
		Minion minion0 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		assertEquals(minion0, minion0);
	}

	@Test
	public void testNotEqualsAttack() {
		Minion minion0 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		Minion minion1 = new Minion("" + 0, (byte)2, (byte)30, (byte)4, (byte)3, (byte)4, (byte)4);
		assertNotEquals(minion0, minion1);
		assertNotEquals(minion0.hashCode(), minion1.hashCode());
	}

	@Test
	public void testNotEqualsBaseAttack() {
		Minion minion0 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		Minion minion1 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)30, (byte)4, (byte)4);
		assertNotEquals(minion0, minion1);
		assertNotEquals(minion0.hashCode(), minion1.hashCode());
	}

	@Test
	public void testNotEqualsBaseHealth() {
		Minion minion0 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		Minion minion1 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)40, (byte)4);
		assertNotEquals(minion0, minion1);
		assertNotEquals(minion0.hashCode(), minion1.hashCode());
	}

	@Test
	public void testNotEqualsDestroyOnTurnEnd() {
		Minion minion0 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		Minion minion1 = (Minion)minion0.deepCopy();
		minion1.setDestroyOnTurnEnd(true);
		assertNotEquals(minion0, minion1);
		assertNotEquals(minion0.hashCode(), minion1.hashCode());
	}

	@Test
	public void testNotEqualsDestroyOnTurnStart() {
		Minion minion0 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		Minion minion1 = (Minion)minion0.deepCopy();
		minion1.setDestroyOnTurnStart(true);
		assertNotEquals(minion0, minion1);
		assertNotEquals(minion0.hashCode(), minion1.hashCode());
	}

	@Test
	public void testNotEqualsHealth() {
		Minion minion0 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		Minion minion1 = new Minion("" + 0, (byte)2, (byte)3, (byte)40, (byte)3, (byte)4, (byte)4);
		assertNotEquals(minion0, minion1);
		assertNotEquals(minion0.hashCode(), minion1.hashCode());
	}

	@Test
	public void testNotEqualsMana() {
		Minion minion0 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		Minion minion1 = new Minion("" + 0, (byte)20, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		assertNotEquals(minion0, minion1);
		assertNotEquals(minion0.hashCode(), minion1.hashCode());
	}

	@Test
	public void testNotEqualsMaxHealth() {
		Minion minion0 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		Minion minion1 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)40);
		assertNotEquals(minion0, minion1);
		assertNotEquals(minion0.hashCode(), minion1.hashCode());
	}

	@Test
	public void testNotEqualsName() {
		Minion minion0 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		Minion minion1 = new Minion("" + 1, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		assertNotEquals(minion0, minion1);
		assertNotEquals(minion0.hashCode(), minion1.hashCode());
	}
}
