package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamageAoe;

public class Hellfire extends SpellDamageAoe {

	private static final byte DAMAGE_AMOUNT = 3;
	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public Hellfire(boolean hasBeenUsed) {
		super((byte)4, DAMAGE_AMOUNT, hasBeenUsed);
		this.hitsEnemyHero = true;
		this.hitsEnemyMinions = true;
		this.hitsOwnMinions = true;
		this.hitsOwnHero = true;
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public Hellfire() {
		this(false);
	}
}
