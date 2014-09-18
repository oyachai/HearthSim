package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;

public class Pyroblast extends SpellDamage {
	
	public Pyroblast() {
		this(false);
	}
	
	public Pyroblast(boolean hasBeenUsed) {
		super("Pyroblast", (byte)10, (byte)10, hasBeenUsed);
	}
	
	@Override
	public Object deepCopy() {
		return new Pyroblast(this.hasBeenUsed);
	}
}
