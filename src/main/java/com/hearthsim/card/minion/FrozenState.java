package com.hearthsim.card.minion;

public class FrozenState implements MinionState{
	
	public Minion handleStateEffect(Minion m) {
		m.hasAttacked_ = true;
		return m;
	}

}
