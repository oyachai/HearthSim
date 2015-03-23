package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.event.MinionFilterSpellTargetable;

public class SinisterStrike extends SpellDamage {

    public SinisterStrike() {
        this.minionFilter = MinionFilterSpellTargetable.OPPONENT;
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
