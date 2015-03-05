package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;

public class SinisterStrike extends SpellDamage {

    public SinisterStrike() {
        this.canTargetEnemyMinions = false;
        this.canTargetOwnHero = false;
        this.canTargetOwnMinions = false;
    }

    @Deprecated
    public SinisterStrike(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    @Override
    public SpellDamage deepCopy() {
        return new SinisterStrike(this.hasBeenUsed);
    }
}
