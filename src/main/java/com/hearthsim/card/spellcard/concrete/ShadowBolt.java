package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;

public class ShadowBolt extends SpellDamage {

    public ShadowBolt() {
        super();

        this.canTargetEnemyHero = false;
        this.canTargetOwnHero = false;
    }

    @Deprecated
    public ShadowBolt(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    @Override
    public SpellDamage deepCopy() {
        return new ShadowBolt(this.hasBeenUsed);
    }
}
