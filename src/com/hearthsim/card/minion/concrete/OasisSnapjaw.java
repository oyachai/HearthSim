package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Beast;

public class OasisSnapjaw extends Beast {

	public OasisSnapjaw() {
		this(
				(byte)4,
				(byte)2,
				(byte)7,
				(byte)2,
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
	
	public OasisSnapjaw(	
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
			"Oasis Snapjaw",
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
