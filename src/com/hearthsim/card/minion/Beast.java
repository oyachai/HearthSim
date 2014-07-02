package com.hearthsim.card.minion;

import com.json.JSONObject;


public class Beast extends Minion {
		
	
	public Beast(
			String name,
			byte mana,
			byte attack,
			byte health,
			byte baseAttack,
			byte baseHealth,
			byte maxHealth,
			boolean taunt,
			boolean divineShield,
			boolean windFury,
			boolean charge,
			boolean hasAttacked,
			boolean hasWindFuryAttacked,
			boolean isInHand,
			boolean hasBeenUsed) {
		super(name, mana, attack, health, baseAttack, baseHealth, maxHealth, taunt, divineShield, windFury, charge, hasAttacked, hasWindFuryAttacked, isInHand, hasBeenUsed);
	}
	public Beast(
			String name,
			byte mana,
			byte attack,
			byte health,
			byte baseAttack,
			byte baseHealth,
			byte maxHealth
			) {
		this(name, mana, attack, health, baseAttack, baseHealth, maxHealth, false, false, false, false, false, false, true, false);
	}
	
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "Beast");
		return json;
	}
}
