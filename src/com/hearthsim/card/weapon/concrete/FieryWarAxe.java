package com.hearthsim.card.weapon.concrete;

import com.hearthsim.card.weapon.WeaponCard;

public class FieryWarAxe extends WeaponCard {

	public FieryWarAxe() {
		this(false);
	}

	public FieryWarAxe(boolean hasBeenUsed) {
		super("Fiery War Axe", (byte)2, (byte)3, (byte)2, hasBeenUsed);
	}

}
