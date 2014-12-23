package com.hearthsim.card.minion;

import org.json.JSONObject;

@Deprecated
public class Pirate extends Minion {

	public Pirate(String name, byte mana, byte attack, byte health, byte baseAttack, byte baseHealth, byte maxHealth) {
		super(name, mana, attack, health, baseAttack, baseHealth, maxHealth);

		this.tribe = MinionTribe.PIRATE;
	}

	public Pirate() {
		super();

		this.tribe = MinionTribe.PIRATE;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "Pirate");
		return json;
	}

}
