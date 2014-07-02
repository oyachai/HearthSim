package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;

public class MagmaRager extends Minion {

	public MagmaRager() {
		this(
				(byte)3,
				(byte)5,
				(byte)1,
				(byte)5,
				(byte)1,
				(byte)1,
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
	
	public MagmaRager(	
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
			"Magma Rager",
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
