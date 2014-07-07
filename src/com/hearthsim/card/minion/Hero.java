package com.hearthsim.card.minion;

import com.hearthsim.util.DeepCopyable;
import com.json.JSONObject;

public class Hero extends Minion {

	public Hero() {
		this("", (byte)30, (byte)30);
	}

	public Hero(String name, byte health, byte maxHealth) {
		super(name, (byte)0, (byte)0, health, (byte)0, maxHealth, maxHealth, false, false, false, false, false, false, false, false, false);
	}

	@Override
	public DeepCopyable deepCopy() {
		return new Hero(this.name_, this.health_, this.maxHealth_);
	}
	
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "Hero");
		return json;
	}
}
