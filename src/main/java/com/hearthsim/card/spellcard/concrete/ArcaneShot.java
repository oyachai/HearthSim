package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;

public class ArcaneShot extends SpellDamage {

    public ArcaneShot() {
        super();
    }

    @Deprecated
    public ArcaneShot(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }
}
