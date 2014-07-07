package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Demon;

public class Voidwalker extends Demon {
	public Voidwalker() {
		this(
				(byte)1,
				(byte)1,
				(byte)3,
				(byte)1,
				(byte)3,
				(byte)3,
				true, //has taunt
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
	
	public Voidwalker(	
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
			"Voidwalker",
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
