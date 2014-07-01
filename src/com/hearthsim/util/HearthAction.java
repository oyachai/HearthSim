package com.hearthsim.util;

import com.hearthsim.card.Card;



public class HearthAction {
	public enum Verb {
		USE, PLACE, BUFF
	}
	
	public class Target {
		
		int playerID_;
		int targetID_;
		
		public Target(int playerID, int targetID) {
			playerID_ = playerID;
			targetID_ = targetID;
		}
		
		public String toString() {
			String toRet = "{\"p\": " + playerID_ + ", \"t\": " + targetID_ + "}";
			return toRet;
		}
		
	}
	
	Verb verb_;
	Card object_;
	Target target1_;
	Target target2_;
	Target target3_;
	Target target4_;
	
	public HearthAction(Verb verb, Card object, Target target) {
		this(verb, object, target, null, null, null);
	}

	public HearthAction(Verb verb, Card object, Target target1, Target target2) {
		this(verb, object, target1, target2, null, null);
	}

	public HearthAction(Verb verb, Card object, Target target1, Target target2, Target target3) {
		this(verb, object, target1, target2, target3, null);
	}

	public HearthAction(Verb verb, Card object, Target target1, Target target2, Target target3, Target target4) {
		verb_ = verb;
		object_ = object;
		target1_ = target1;
		target2_ = target2;
		target3_ = target3;
		target4_ = target4;
	}
	
	public String toString() {
		String toRet = "{\"verb\": ";
		switch (verb_) {
		case USE:
			toRet = toRet + "\"USE\"";
		case PLACE:
			toRet = toRet + "\"PLACE\"";
		}
		
		toRet = toRet + ", \"card\": " + object_;
		toRet = toRet + ", \"target1\": " + target1_;
		toRet = toRet + ", \"target2\": " + target2_;
		toRet = toRet + ", \"target3\": " + target3_;
		toRet = toRet + ", \"target4\": " + target4_;
		toRet = toRet + "}";
		
		return toRet;
	}
}
