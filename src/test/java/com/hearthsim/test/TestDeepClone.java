package com.hearthsim.test;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.AcidicSwampOoze;
import com.hearthsim.entity.BaseEntity;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestDeepClone {

	
	@Test
	public void test_AcidicSwampOoze() {
		

		BaseEntity card1 = new AcidicSwampOoze();
		BaseEntity card1_cloned = (BaseEntity) card1.deepCopy();
		
		assertTrue(card1.equals(card1_cloned));
		assertTrue(card1_cloned.equals(card1));
		
		card1.setHealth((byte)(card1.getHealth() + 1));
		assertFalse(card1.equals(card1_cloned));
		assertFalse(card1_cloned.equals(card1));

		card1_cloned = (BaseEntity) card1.deepCopy();
		assertTrue(card1.equals(card1_cloned));
		assertTrue(card1_cloned.equals(card1));

		card1.setAttack((byte)(card1.getAttack() + 1));
		assertFalse(card1.equals(card1_cloned));
		assertFalse(card1_cloned.equals(card1));

		card1_cloned = (BaseEntity)card1.deepCopy();
		assertTrue(card1.equals(card1_cloned));
		assertTrue(card1_cloned.equals(card1));
		
		card1.setDestroyOnTurnEnd(true);
		assertFalse(card1.equals(card1_cloned));
		assertFalse(card1_cloned.equals(card1));
		card1_cloned = (BaseEntity)card1.deepCopy();
		assertTrue(card1.equals(card1_cloned));
		assertTrue(card1_cloned.equals(card1));

		card1.setDestroyOnTurnStart(true);
		assertFalse(card1.equals(card1_cloned));
		assertFalse(card1_cloned.equals(card1));
		card1_cloned = (BaseEntity)card1.deepCopy();
		assertTrue(card1.equals(card1_cloned));
		assertTrue(card1_cloned.equals(card1));

	}
}
