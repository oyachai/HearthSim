package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Beast;

public class Frog extends Beast {

	public Frog() {
		this(
				(byte)0,
				(byte)0,
				(byte)1,
				(byte)0,
				(byte)1,
				(byte)1,
				true, //has taunt
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
		
	public Frog(	
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
							boolean isInHand,
							boolean hasBeenUsed) {
		
		super(
			"Frog",
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
			isInHand,
			hasBeenUsed);
	}
}
