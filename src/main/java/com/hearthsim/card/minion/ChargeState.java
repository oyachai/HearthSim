package com.hearthsim.card.minion;

public class ChargeState implements MinionState{

	public Minion handleStateEffect(Minion m) {
		m.hasAttacked_ = false;
		return m;
	}

}
