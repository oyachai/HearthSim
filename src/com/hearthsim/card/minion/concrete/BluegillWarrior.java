package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;

public class BluegillWarrior extends Minion {

	public BluegillWarrior() {
		this(
				(byte)2,
				(byte)2,
				(byte)1,
				(byte)2,
				(byte)1,
				(byte)1,
				false,
				false,
				false,
				true, //has charge
				false,
				false,
				false,
				false,
				false,
				true,
				false
			);
	}
	
	public BluegillWarrior(	
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
			"Bluegill Warrior",
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
