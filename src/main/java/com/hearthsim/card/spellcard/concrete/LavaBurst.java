package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;

public class LavaBurst extends SpellDamage {

    public LavaBurst() {
        super();
    }

    @Deprecated
    public LavaBurst(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }
}
