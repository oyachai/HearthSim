package com.hearthsim.card.minion;

import com.json.JSONObject;

public class Totem extends Minion {

	public Totem(
			String name,
			byte mana,
			byte attack,
			byte health,
			byte baseAttack,
			byte extraAttackUntilTurnEnd,
			byte baseHealth,
			byte maxHealth,
			boolean taunt,
			boolean divineShield,
			boolean windFury,
			boolean charge,
			boolean hasAttacked,
			boolean hasWindFuryAttacked,
			boolean frozen,
			boolean summoned,
			boolean transformed,
			boolean destroyOnTurnStart,
			boolean destroyOnTurnEnd,
			boolean isInHand,
			boolean hasBeenUsed) {
		super(
				name,
				mana,
				attack,
				health,
				baseAttack,
				extraAttackUntilTurnEnd,
				baseHealth,
				maxHealth,
				taunt,
				divineShield,
				windFury,
				charge,
				hasAttacked,
				hasWindFuryAttacked,
				frozen,
				summoned,
				transformed,
				destroyOnTurnStart,
				destroyOnTurnEnd,
				isInHand,
				hasBeenUsed);
	}
	
	public Totem(
			String name,
			byte mana,
			byte attack,
			byte health,
			byte baseAttack,
			byte baseHealth,
			byte maxHealth
			) {
		this(name, mana, attack, health, baseAttack, (byte)0, baseHealth, maxHealth, false, false, false, false, false, false, false, false, false, false, false, true, false);
	}
	
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "Totem");
		return json;
	}
}
