package com.hearthsim.card.weapon.concrete;

import com.hearthsim.card.weapon.WeaponCard;

public class AssassinsBlade extends WeaponCard {

	public AssassinsBlade() {
		this(false);
	}

	public AssassinsBlade(boolean hasBeenUsed) {
		super("Assassin's Blade", (byte)5, (byte)3, (byte)4, hasBeenUsed);
	}

}
