package com.hearthsim.card.minion;

import com.json.JSONObject;


public class Murloc extends Minion {

	
	public Murloc(
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
			boolean silenced,
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
				silenced,
				summoned,
				transformed,
				destroyOnTurnStart,
				destroyOnTurnEnd,
				isInHand,
				hasBeenUsed);
	}
	
	public Murloc(String name, byte mana, byte attack, byte health,
			byte baseAttack,
			byte baseHealth,
			byte maxHealth
			) 
	{
		this(
				name,
				mana,
				attack,
				health,
				baseAttack,
				(byte)0,
				baseHealth,
				maxHealth,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				true,
				false);
	}
	
	@Override
	public Object deepCopy() {
		return new Murloc(
				this.name_,
				this.mana_,
				this.attack_,
				this.health_,
				this.baseAttack_,
				this.extraAttackUntilTurnEnd_,
				this.baseHealth_,
				this.maxHealth_,
				this.taunt_,
				this.divineShield_,
				this.windFury_,
				this.charge_,
				this.hasAttacked_,
				this.hasWindFuryAttacked_,
				this.frozen_,
				this.silenced_,
				this.summoned_,
				this.transformed_,
				this.destroyOnTurnStart_,
				this.destroyOnTurnEnd_,
				this.isInHand_,
				this.hasBeenUsed_);
	}
	
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "Murloc");
		return json;
	}
}
