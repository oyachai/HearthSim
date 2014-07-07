package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Beast;
import com.hearthsim.card.minion.Minion;

public class SenjinShieldmasta extends Minion {
	
	public SenjinShieldmasta() {
		this(
				(byte)4,
				(byte)3,
				(byte)5,
				(byte)3,
				(byte)5,
				(byte)5,
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
		
	public SenjinShieldmasta(	
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
			"Sen'jin Shieldmasta",
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
