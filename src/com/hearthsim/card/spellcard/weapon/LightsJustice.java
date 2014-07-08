package com.hearthsim.card.spellcard.weapon;

import com.hearthsim.card.spellcard.WeaponCard;

public class LightsJustice extends WeaponCard {

	public LightsJustice() {
		this(false);
	}

	public LightsJustice(boolean hasBeenUsed) {
		super("Light's Justice", (byte)1, (byte)1, (byte)4, hasBeenUsed);
	}

}
