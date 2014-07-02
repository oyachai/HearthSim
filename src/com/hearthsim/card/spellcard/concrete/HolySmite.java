package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;

public class HolySmite extends SpellDamage {
	
	public HolySmite() {
		this(false);
	}
	
	public HolySmite(boolean hasBeenUsed) {
		super("Holy Smite", (byte)1, (byte)2, hasBeenUsed);
	}

}
