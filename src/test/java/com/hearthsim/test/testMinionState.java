package com.hearthsim.test;

import com.hearthsim.card.minion.FrozenState;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionState;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class testMinionState {
	
	Minion m1;
	Minion m2;
	MinionState fs;
	
	@Before
	public void setup() throws HSException {
		 m1 = new Minion(); 
		 m2 = new Minion();
		 fs = new FrozenState();
	}
	
	@Test
	public void test0() throws HSException{
		
		m1.addState(fs);
		assertTrue(m1.getState(fs) != null);
		m1.removeState(fs);
		assertTrue(m1.getState(fs) == null);
	}
	
	@Test
	public void test1() throws HSException{
		m1.removeState(fs);
		assertTrue(m1.getState(fs) == null);
	}
	
	

}
