package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;

public class AlAkirTheWindlord extends Minion {

	private static final String NAME = "Al'Akir the Windlord";
	private static final byte MANA_COST = 8;
	private static final byte ATTACK = 3;
	private static final byte HEALTH = 5;
	
	private static final boolean TAUNT = true;
	private static final boolean DIVINE_SHIELD = true;
	private static final boolean WINDFURY = true;
	private static final boolean CHARGE = true;
	
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	
	public AlAkirTheWindlord() {
		this(
				MANA_COST,
				ATTACK,
				HEALTH,
				ATTACK,
				(byte)0,
				HEALTH,
				HEALTH,
				TAUNT,
				DIVINE_SHIELD,
				WINDFURY,
				CHARGE,
				false,
				false,
				false,
				false,
				SUMMONED,
				TRANSFORMED,
				false,
				false,
				true,
				false
			);
	}
	
	public AlAkirTheWindlord(	
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
			NAME,
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
	
	@Override
	public Object deepCopy() {
		return new AlAkirTheWindlord(
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
}
