package com.hearthsim.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import com.hearthsim.card.minion.Minion;

public class TestMinion {
	@Test
	public void testEqualsDeepCopy() {
		Minion minion0 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		Minion copy = (Minion)minion0.deepCopy();
		assertEquals(minion0, copy);

		copy.setAttack((byte)10);
		assertNotEquals(minion0, copy);
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
	}

	@Test
	public void testNotEqualsBaseAttack() {
		Minion minion0 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		Minion minion1 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)30, (byte)4, (byte)4);
		assertNotEquals(minion0, minion1);
	}

	@Test
	public void testNotEqualsBaseHealth() {
		Minion minion0 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		Minion minion1 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)40, (byte)4);
		assertNotEquals(minion0, minion1);
	}

	@Test
	public void testNotEqualsHealth() {
		Minion minion0 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		Minion minion1 = new Minion("" + 0, (byte)2, (byte)3, (byte)40, (byte)3, (byte)4, (byte)4);
		assertNotEquals(minion0, minion1);
	}

	@Test
	public void testNotEqualsMana() {
		Minion minion0 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		Minion minion1 = new Minion("" + 0, (byte)20, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		assertNotEquals(minion0, minion1);
	}

	@Test
	public void testNotEqualsMaxHealth() {
		Minion minion0 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		Minion minion1 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)40);
		assertNotEquals(minion0, minion1);
	}

	@Test
	public void testNotEqualsName() {
		Minion minion0 = new Minion("" + 0, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		Minion minion1 = new Minion("" + 1, (byte)2, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4);
		assertNotEquals(minion0, minion1);
	}
}
