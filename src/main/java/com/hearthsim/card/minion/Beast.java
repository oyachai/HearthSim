package com.hearthsim.card.minion;

import org.json.JSONObject;


public class Beast extends Minion {
    //TODO: replace all the race intermediary classes with race attribute on minion

    public Beast() {
        super();
    }

    public Beast(String name, byte mana, byte attack, byte health, byte baseAttack, byte baseHealth, byte maxHealth) {
        super(name, mana, attack, health, baseAttack, baseHealth, maxHealth);
    }

    public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "Beast");
		return json;
	}
}
