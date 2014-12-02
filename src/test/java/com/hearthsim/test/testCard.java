package com.hearthsim.test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.entity.BaseEntity;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class testCard {

	@Test
	public void testCard0() {
		
		byte mana = 2;
		
		Card card0 = new Card("" + 0, mana);
		Card card1 = new Card("" + 1, mana);
		Card card2 = new Card("" + 0, (byte)(mana + 1));
		
		assertTrue("test0", card0.equals(card0));
		assertTrue("test1", card1.equals(card1));
		assertTrue("test2", card2.equals(card2));
		assertFalse("test1", card0.equals(card1));
		assertFalse("test1", card0.equals(card2));
		assertFalse("test1", card1.equals(card0));
		assertFalse("test2", card1.equals(card2));
		assertFalse("test1", card2.equals(card0));
		assertFalse("test2", card2.equals(card1));
		
		
		Card card0_clone = (Card)card0.deepCopy();
		Card card1_clone = (Card)card1.deepCopy();
		Card card2_clone = (Card)card2.deepCopy();
		
		assertTrue("test_clone_0", card0.equals(card0_clone) && card0_clone.equals(card0));
		assertTrue("test_clone_1", card1.equals(card1_clone) && card1_clone.equals(card1));
		assertTrue("test_clone_2", card2.equals(card2_clone) && card2_clone.equals(card2));
	
		assertFalse("test_clone_unequal_1", card0.equals(card1_clone));
		assertFalse("test_clone_unequal_1", card0.equals(card2_clone));
		assertFalse("test_clone_unequal_1", card1.equals(card0_clone));
		assertFalse("test_clone_unequal_1", card1.equals(card2_clone));
		assertFalse("test_clone_unequal_1", card2.equals(card1_clone));
		assertFalse("test_clone_unequal_1", card2.equals(card0_clone));

		assertFalse("test_clone_unequal_1", card0_clone.equals(card1));
		assertFalse("test_clone_unequal_1", card0_clone.equals(card2));
		assertFalse("test_clone_unequal_1", card1_clone.equals(card0));
		assertFalse("test_clone_unequal_1", card1_clone.equals(card2));
		assertFalse("test_clone_unequal_1", card2_clone.equals(card1));
		assertFalse("test_clone_unequal_1", card2_clone.equals(card0));
	}
	
	@Test
	public void testMinion() {
		
		byte attack = 3;
		byte health = 2;
		byte mana = 2;
		
		Minion minion0 = new Minion("" + 0, mana, attack, health, attack, health, health);
		Minion minion1 = new Minion("" + 1, mana, attack, health, attack, health, health);
		Minion minion2 = new Minion("" + 0, mana, (byte)(attack + 1), health, attack, health, health);
		Minion minion3 = new Minion("" + 0, mana, attack, (byte)(health + 1), attack, health, health);
		Minion minion4 = new Minion("" + 0, (byte)(mana + 1), attack, health, attack, health, health);
		Minion minion5 = new Minion("" + 0, mana, attack, health, (byte)(attack + 1), health, health);
		Minion minion6 = new Minion("" + 0, mana, attack, health, attack, (byte)(health + 1), health);
		Minion minion7 = new Minion("" + 0, mana, attack, health, attack, health, (byte)(health + 1));
		
		assertTrue("test0", minion0.equals(minion0));
		assertFalse("test1", minion0.equals(minion1));
		assertFalse("test2", minion0.equals(minion2));
		assertFalse("test3", minion0.equals(minion3));
		assertFalse("test4", minion0.equals(minion4));
		assertFalse("test5", minion0.equals(minion5));
		assertFalse("test6", minion0.equals(minion6));
		assertFalse("test7", minion0.equals(minion7));
		
		
		BaseEntity minion0_clone = (BaseEntity) minion0.deepCopy();
		BaseEntity minion1_clone = (BaseEntity) minion1.deepCopy();
		BaseEntity minion2_clone = (BaseEntity) minion2.deepCopy();
		BaseEntity minion3_clone = (BaseEntity) minion3.deepCopy();
		BaseEntity minion4_clone = (BaseEntity) minion4.deepCopy();
		BaseEntity minion5_clone = (BaseEntity) minion5.deepCopy();
		BaseEntity minion6_clone = (BaseEntity) minion6.deepCopy();
		BaseEntity minion7_clone = (BaseEntity) minion7.deepCopy();
		
		assertTrue("test_clone_0", minion0.equals(minion0_clone) && minion0_clone.equals(minion0));
		assertTrue("test_clone_1", minion1.equals(minion1_clone) && minion1_clone.equals(minion1));
		assertTrue("test_clone_2", minion2.equals(minion2_clone) && minion2_clone.equals(minion2));
		assertTrue("test_clone_3", minion3.equals(minion3_clone) && minion3_clone.equals(minion3));
		assertTrue("test_clone_4", minion4.equals(minion4_clone) && minion4_clone.equals(minion4));
		assertTrue("test_clone_5", minion5.equals(minion5_clone) && minion5_clone.equals(minion5));
		assertTrue("test_clone_6", minion6.equals(minion6_clone) && minion6_clone.equals(minion6));
		assertTrue("test_clone_7", minion7.equals(minion7_clone) && minion7_clone.equals(minion7));
	
		assertFalse("test_clone_unequal_1", minion0.equals(minion1_clone));
		assertFalse("test_clone_unequal_2", minion0.equals(minion2_clone));
		assertFalse("test_clone_unequal_3", minion0.equals(minion3_clone));
		assertFalse("test_clone_unequal_4", minion0.equals(minion4_clone));
		assertFalse("test_clone_unequal_5", minion0.equals(minion5_clone));
		assertFalse("test_clone_unequal_6", minion0.equals(minion6_clone));
		assertFalse("test_clone_unequal_7", minion0.equals(minion7_clone));
	}
	

}
