package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Beast;

public class StonetuskBoar extends Beast {
	public StonetuskBoar() {
		this(
				(byte)1,
				(byte)1,
				(byte)1,
				(byte)1,
				(byte)1,
				(byte)1,
				false,
				false,
				false,
				true, //has charge
				false,
				false,
				false,
				true,
				false
			);
	}
	
	public StonetuskBoar(	
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
			"Stonetusk Board",
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
