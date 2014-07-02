package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Beast;

public class BloodfenRaptor extends Beast {

	public BloodfenRaptor() {
		this(
				(byte)2,
				(byte)3,
				(byte)2,
				(byte)3,
				(byte)2,
				(byte)2,
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
	
	public BloodfenRaptor(	
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
			"Bloodfen Raptor",
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
