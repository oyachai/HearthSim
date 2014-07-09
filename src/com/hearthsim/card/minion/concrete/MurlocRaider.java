package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Murloc;

public class MurlocRaider extends Murloc {

	public MurlocRaider() {
		this(
				(byte)1,
				(byte)2,
				(byte)1,
				(byte)2,
				(byte)1,
				(byte)1,
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
				false
			);
	}
	
	public MurlocRaider(	
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
							boolean frozen,
							boolean summoned,
							boolean transformed,
							boolean isInHand,
							boolean hasBeenUsed) {
		
		super(
			"Murloc Raider",
			mana,
			attack,
			health,
			baseAttack,
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
			isInHand,
			hasBeenUsed);
	}
}
