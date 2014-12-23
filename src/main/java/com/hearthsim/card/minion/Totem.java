package com.hearthsim.card.minion;

import org.json.JSONObject;

@Deprecated
public class Totem extends Minion {

	public Totem() {
		super();

		this.tribe = MinionTribe.TOTEM;
	}

	public Totem(String name, byte mana, byte attack, byte health, byte baseAttack, byte baseHealth, byte maxHealth) {
		super(name, mana, attack, health, baseAttack, baseHealth, maxHealth);

		this.tribe = MinionTribe.TOTEM;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "Totem");
		return json;
	}
}
