package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;

public class Moonfire extends SpellDamage {
	
	public Moonfire() {
		this(false);
	}

	public Moonfire(boolean hasBeenUsed) {
		super((byte)0, (byte)1, hasBeenUsed);
	}

	
	public Object deepCopy() {
		return new Moonfire(this.hasBeenUsed);
	}
}
