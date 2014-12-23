package com.hearthsim.card.minion;

import org.json.JSONObject;

@Deprecated
public class Dragon extends Minion {

	public Dragon() {
		super();

		this.tribe = MinionTribe.DRAGON;
	}

	public Dragon(String name, byte mana, byte attack, byte health, byte baseAttack, byte baseHealth, byte maxHealth) {
		super(name, mana, attack, health, baseAttack, baseHealth, maxHealth);

		this.tribe = MinionTribe.DRAGON;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "Beast");
		return json;
	}
}
