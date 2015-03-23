package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.event.MinionFilterTargetedSpell;

public class SinisterStrike extends SpellDamage {

    public SinisterStrike() {
        this.minionFilter = MinionFilterTargetedSpell.OPPONENT;
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
