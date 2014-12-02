package com.hearthsim.card.minion;

public class MinionStateFactory {
	
	public MinionState makeFrozen(){
		return new FrozenState();
	}
	
	public MinionState makeStealthed(){
		return new StealthedState();
	}
	
	public MinionState makeCharge(){
		return new ChargeState();
	}
	
	public MinionState makeWindfury(){
		return new WindfuryState();
	}

}
