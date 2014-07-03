package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;

public class RecklessRocketeer extends Minion {
	public RecklessRocketeer() {
		this(
				(byte)6,
				(byte)5,
				(byte)2,
				(byte)5,
				(byte)2,
				(byte)2,
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
	
	public RecklessRocketeer(	
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
			"Reckless Rocketeer",
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
