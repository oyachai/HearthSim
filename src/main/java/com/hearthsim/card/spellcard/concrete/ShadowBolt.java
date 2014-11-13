package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;

public class ShadowBolt extends SpellDamage {

	public ShadowBolt() {
		this(false);
	}
	
	public ShadowBolt(boolean hasBeenUsed) {
		super((byte)3, (byte)4, hasBeenUsed);

		this.canTargetEnemyHero = false;
		this.canTargetOwnHero = false;
	}
	
	@Override
	public SpellDamage deepCopy() {
		return new ShadowBolt(this.hasBeenUsed);
	}
}
