package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamageAoe;

public class ArcaneExplosion extends SpellDamageAoe {

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public ArcaneExplosion(boolean hasBeenUsed) {
		super((byte)2, (byte)1, hasBeenUsed);
	}

	/**
	 * Constructor
	 * Defaults to hasBeenUsed = false
	 */
	public ArcaneExplosion() {
		this(false);
	}
}
