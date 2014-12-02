package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;

public class SinisterStrike extends SpellDamage {

	public SinisterStrike() {
		this(false);
	}

	public SinisterStrike(boolean hasBeenUsed) {
		super((byte)1, (byte)3, hasBeenUsed);

		this.canTargetEnemyMinions = false;
		this.canTargetOwnHero = false;
		this.canTargetOwnMinions = false;
	}

	@Override
	public SpellDamage deepCopy() {
		return new 	SinisterStrike(this.hasBeenUsed);
	}
}
