package com.hearthsim.card.weapon.concrete;

import com.hearthsim.card.weapon.WeaponCard;

public class ArcaniteReaper extends WeaponCard {

	public ArcaniteReaper() {
		this(false);
	}

	public ArcaniteReaper(boolean hasBeenUsed) {
		super("Arcanite Reaper", (byte)5, (byte)5, (byte)2, hasBeenUsed);
	}

}
