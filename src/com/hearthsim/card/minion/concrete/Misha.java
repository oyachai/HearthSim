package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Beast;

public class Misha extends Beast {

	public Misha() {
		this(
				(byte)3,
				(byte)4,
				(byte)4,
				(byte)4,
				(byte)4,
				(byte)4,
				true, //has taunt
				false,
				false,
				false,
				false,
				false,
				false,
				true, //by default summoned
				false,
				true,
				false
			);
	}
		
	public Misha(	
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
			"Misha",
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
