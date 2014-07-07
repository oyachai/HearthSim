package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;

public class NorthshireCleric extends Minion {

	public NorthshireCleric() {
		this(
				(byte)1,
				(byte)1,
				(byte)3,
				(byte)1,
				(byte)3,
				(byte)3,
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
	
	public NorthshireCleric(	
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
		
		super(
			"Northshire Cleric",
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
			isInHand,
			hasBeenUsed);
	}
}
