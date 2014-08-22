package com.hearthsim.card.weapon.concrete;

import com.hearthsim.card.weapon.WeaponCard;

public class LightsJustice extends WeaponCard {

	public LightsJustice() {
		this(false);
	}

	public LightsJustice(boolean hasBeenUsed) {
		super("Light's Justice", (byte)1, (byte)1, (byte)4, hasBeenUsed);
	}

}
