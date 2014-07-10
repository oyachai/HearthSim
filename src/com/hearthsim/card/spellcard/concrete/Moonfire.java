package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;

public class Moonfire extends SpellDamage {
	
	public Moonfire() {
		this(false);
	}

	public Moonfire(boolean hasBeenUsed) {
		super("Moonfire", (byte)0, (byte)1, hasBeenUsed);
	}

	@Override
	public Object deepCopy() {
		return new Moonfire(this.hasBeenUsed_);
	}
}
