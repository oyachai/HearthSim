package com.hearthsim.card.minion;

public class WindfuryState implements MinionState{

	@Override
	public Minion handleStateEffect(Minion m) {
		m.hasWindFuryAttacked_ = true;
		return m;
	}

}
