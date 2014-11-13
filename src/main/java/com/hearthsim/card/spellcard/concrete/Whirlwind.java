package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamageAoe;

public class Whirlwind extends SpellDamageAoe {

	private static final byte DAMAGE_AMOUNT = 1;
	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public Whirlwind(boolean hasBeenUsed) {
		super((byte)1, DAMAGE_AMOUNT, hasBeenUsed);

		this.hitsEnemyMinions = true;
		this.hitsOwnMinions = true;
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public Whirlwind() {
		this(false);
	}
}
