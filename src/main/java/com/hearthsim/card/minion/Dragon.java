package com.hearthsim.card.minion;

import org.json.JSONObject;

public class Dragon extends Minion {

    public Dragon() {
    }

    public Dragon(String name, byte mana, byte attack, byte health, byte baseAttack, byte baseHealth, byte maxHealth) {
        super(name, mana, attack, health, baseAttack, baseHealth, maxHealth);
    }

    @Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "Beast");
		return json;
	}
}
