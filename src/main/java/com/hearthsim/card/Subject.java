package com.hearthsim.card;

import java.util.ArrayList;

public class Subject {
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	
	public void attach( Observer o ) {
		observers.add(o);
	}
	
	public void notifyObservers() {
		for (int i=0; i < observers.size(); i++){
			observers.get(i).update();
		}
	}
}
