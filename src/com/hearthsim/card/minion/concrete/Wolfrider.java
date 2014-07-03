package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;

public class Wolfrider extends Minion {
	public Wolfrider() {
		this(
				(byte)3,
				(byte)3,
				(byte)1,
				(byte)3,
				(byte)1,
				(byte)1,
				false,
				false,
				false,
				true, //has charge
				false,
				false,
				true,
				false
			);
	}
	
	public Wolfrider(	
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
			"Wolfrider",
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
