package com.hearthsim.card.minion;

import org.json.JSONObject;

@Deprecated
public class Demon extends Minion {

	public Demon(String name, byte mana, byte attack, byte health, byte baseAttack, byte baseHealth, byte maxHealth) {
		super(name, mana, attack, health, baseAttack, baseHealth, maxHealth);

		this.tribe = MinionTribe.DEMON;
	}

	public Demon() {
		super();

		this.tribe = MinionTribe.DEMON;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "Demon");
		return json;
	}
}
