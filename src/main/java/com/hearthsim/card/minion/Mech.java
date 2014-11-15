package com.hearthsim.card.minion;

import org.json.JSONObject;

public class Mech extends Minion {
    public Mech(String name, byte mana, byte attack, byte health, byte baseAttack, byte baseHealth, byte maxHealth) {
        super(name, mana, attack, health, baseAttack, baseHealth, maxHealth);
    }

    public Mech() {
    }

    public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "Mech");
		return json;
	}

}
