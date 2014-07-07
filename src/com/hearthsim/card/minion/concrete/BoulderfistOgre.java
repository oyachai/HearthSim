package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;

public class BoulderfistOgre extends Minion {

	public BoulderfistOgre() {
		this(
				(byte)6,
				(byte)6,
				(byte)7,
				(byte)6,
				(byte)7,
				(byte)7,
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
	
	public BoulderfistOgre(	
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
			"Boulderfist Ogre",
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
