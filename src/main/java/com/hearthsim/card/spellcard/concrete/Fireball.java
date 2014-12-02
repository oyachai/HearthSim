package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;

public class Fireball extends SpellDamage {
	
	public Fireball() {
		this(false);
	}

	public Fireball(boolean hasBeenUsed) {
		super((byte)4, (byte)6, hasBeenUsed);
	}

	
	public Object deepCopy() {
		return new Fireball(this.hasBeenUsed);
	}
}
