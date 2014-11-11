package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;

public class Pyroblast extends SpellDamage {
	
	public Pyroblast() {
		this(false);
	}
	
	public Pyroblast(boolean hasBeenUsed) {
		super((byte)10, (byte)10, hasBeenUsed);
	}
	
	@Override
	public SpellDamage deepCopy() {
		return new Pyroblast(this.hasBeenUsed);
	}
}
