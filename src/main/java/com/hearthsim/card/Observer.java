package com.hearthsim.card;

public abstract class Observer {
	protected Subject subject;
	
	public abstract void update();
}
