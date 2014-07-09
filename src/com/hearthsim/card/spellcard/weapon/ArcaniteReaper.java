package com.hearthsim.card.spellcard.weapon;

import com.hearthsim.card.spellcard.WeaponCard;

public class ArcaniteReaper extends WeaponCard {

	public ArcaniteReaper() {
		this(false);
	}

	public ArcaniteReaper(boolean hasBeenUsed) {
		super("Arcanite Reaper", (byte)5, (byte)5, (byte)2, hasBeenUsed);
	}

}
