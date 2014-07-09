package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;

public class BootyBayBodyguard extends Minion {

	public BootyBayBodyguard() {
		this(
				(byte)5,
				(byte)5,
				(byte)4,
				(byte)5,
				(byte)4,
				(byte)4,
				true, //has taunt
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
		
	public BootyBayBodyguard(	
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
			"Booty Bay Bodyguard",
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
