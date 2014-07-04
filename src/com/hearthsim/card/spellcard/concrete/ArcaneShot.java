package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;

public class ArcaneShot extends SpellDamage {
	
	public ArcaneShot() {
		this(false);
	}

	public ArcaneShot(boolean hasBeenUsed) {
		super("Arcane Shot", (byte)1, (byte)2, hasBeenUsed);
	}
}
