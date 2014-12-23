package com.hearthsim.card.minion;

import org.json.JSONObject;

@Deprecated
public class Mech extends Minion {
	public Mech(String name, byte mana, byte attack, byte health, byte baseAttack, byte baseHealth, byte maxHealth) {
		super(name, mana, attack, health, baseAttack, baseHealth, maxHealth);

		this.tribe = MinionTribe.MECH;
	}

	public Mech() {
		super();

		this.tribe = MinionTribe.MECH;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "Mech");
		return json;
	}

}
