package com.hearthsim.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.BoardStateFactory;
import com.hearthsim.util.HearthTreeNode;
import com.hearthsim.card.spellcard.concrete.*;

public class TestTaunt {
	
	private BoardState board;
	private static final byte mana = 2;
	private static final byte attack0 = 2;
	private static final byte health0 = 3;
	private static final byte health1 = 7;

	@Before
	public void setup() {
		board = new BoardState();

		Minion minion0_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		
		board.placeMinion_p0(minion0_0);
				
	}
	
	@Test
	public void test0() {
		Minion minion = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1, true, false, false, false, false, false, false, false);
		board.placeMinion_p1(minion);
		
		BoardStateFactory factory = new BoardStateFactory(null);
		HearthTreeNode<BoardState> tree = new HearthTreeNode<BoardState>(board);
		tree = factory.doMoves(tree);
		
		assertTrue("test0", tree.numChildren() == 2);
		for (HearthTreeNode<BoardState> child : tree.getChildren()) {
			assertTrue("test0", child.numChildren() == 0);
		}
	}

	@Test
	public void test1() {
		Minion minion1 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1, true, false, false, false, false, false, false, false);
		Minion minion2 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1, false, false, false, false, false, false, false, false);
		board.placeMinion_p1(minion1);
		board.placeMinion_p1(minion2);
		
		BoardStateFactory factory = new BoardStateFactory(null);
		HearthTreeNode<BoardState> tree = new HearthTreeNode<BoardState>(board);
		tree = factory.doMoves(tree);
		
		assertTrue("test1", tree.numChildren() == 2);
		for (HearthTreeNode<BoardState> child : tree.getChildren()) {
			assertTrue("test1", child.numChildren() == 0);
		}
	}

	@Test
	public void test2() {
		Minion minion1 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1, true, false, false, false, false, false, false, false);
		Minion minion2 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1, false, false, false, false, false, false, false, false);
		Minion minion3 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1, true, false, false, false, false, false, false, false);
		board.placeMinion_p1(minion1);
		board.placeMinion_p1(minion2);
		board.placeMinion_p1(minion3);
		
		BoardStateFactory factory = new BoardStateFactory(null);
		HearthTreeNode<BoardState> tree = new HearthTreeNode<BoardState>(board);
		tree = factory.doMoves(tree);
		
		assertTrue("test2", tree.numChildren() == 3);
		for (HearthTreeNode<BoardState> child : tree.getChildren()) {
			assertTrue("test2", child.numChildren() == 0);
		}
	}
	
	@Test
	public void test3() {
		HolySmite hs = new HolySmite();
		Minion minion1 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1, true, false, false, false, false, false, false, false);

		board.removeMinion_p0(0);
		board.placeCard_hand(hs);
		board.placeMinion_p1(minion1);
		
		BoardStateFactory factory = new BoardStateFactory(null);
		HearthTreeNode<BoardState> tree = new HearthTreeNode<BoardState>(board);
		tree = factory.doMoves(tree);
				
		assertTrue("test3", tree.numChildren() == 1);
		for (HearthTreeNode<BoardState> child : tree.getChildren()) {
			assertTrue("test3", child.numChildren() == 0);
		}
	}

	@Test
	public void test4() {
		HolySmite hs = new HolySmite();
		Minion minion1 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1, true, false, false, false, false, false, false, false);

		board.removeMinion_p0(0);
		board.placeCard_hand(hs);
		board.placeMinion_p1(minion1);
		board.setMana_p0(2);
		
		BoardStateFactory factory = new BoardStateFactory(null);
		HearthTreeNode<BoardState> tree = new HearthTreeNode<BoardState>(board);
		tree = factory.doMoves(tree);
				
		assertTrue("test4", tree.numChildren() == 4);
		for (HearthTreeNode<BoardState> child : tree.getChildren()) {
			assertTrue("test4", child.numChildren() == 0);
		}
	}

}
