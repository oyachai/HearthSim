package com.hearthsim.test;

import com.hearthsim.card.minion.ChargeState;
import com.hearthsim.card.minion.FrozenState;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionState;
import com.hearthsim.card.minion.StealthedState;
import com.hearthsim.card.minion.WindfuryState;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class testMinionState {
	
	public Minion m1;
	public Minion m2;
	public MinionState fs;
	public MinionState cs;
	public MinionState ss;
	public MinionState wf;
	
	@Before
	public void setup() throws HSException {
		 m1 = new Minion(); 
		 m2 = new Minion();
		 fs = new FrozenState();
		 cs = new ChargeState();
		 ss = new StealthedState();
		 wf = new WindfuryState();
	}
	
	@Test
	public void testFrozenState() throws HSException{
		m1.addState(fs);
		assertTrue(m1.getState(fs) != null);
		m1.removeState(fs);
		assertTrue(m1.getState(fs) == null);
		assertTrue(m1.hasAttacked() == m2.hasAttacked());
		fs.handleStateEffect(m1);
		assertFalse(m1.hasAttacked() == m2.hasAttacked());
	}
	
	@Test
	public void testChargeState() throws HSException{
		m1.addState(cs);
		assertTrue(m1.getState(cs) != null);
		m1.removeState(cs);
		assertTrue(m1.getState(cs) == null);
		m1.addState(cs);
		assertTrue(m1.hasAttacked() == m2.hasAttacked());
		cs.handleStateEffect(m1);
		assertTrue(m1.hasAttacked() == false);
	}
	
	@Test
	public void testStealthedState() throws HSException{
		m1.addState(ss);
		assertTrue(m1.getState(ss) != null);
		m1.removeState(ss);
		assertTrue(m1.getState(ss) == null);
		m1.addState(ss);
		// stealth doesn't have any state effect, dev call it directly from the Minion class
		
	}
	
	@Test
	public void testWindfuryState() throws HSException{
		assertFalse(m1.hasWindFuryAttacked());
		m1.addState(wf);
		assertTrue(m1.getState(wf) != null);
		m1.removeState(wf);
		assertTrue(m1.getState(wf) == null);
		m1.addState(wf);
		wf.handleStateEffect(m1);
		assertTrue(m1.hasWindFuryAttacked());
		
	}
	
	

}
