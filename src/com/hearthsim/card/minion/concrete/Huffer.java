package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Beast;

public class Huffer extends Beast {

	
	public Huffer() {
		this(
				(byte)3,
				(byte)4,
				(byte)2,
				(byte)4,
				(byte)2,
				(byte)2,
				false,
				false,
				false,
				true, //has charge
				false,
				false,
				false,
				true, //by default summoned
				false,
				true,
				false
			);
	}
	
	public Huffer(	
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
			"Huffer",
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
