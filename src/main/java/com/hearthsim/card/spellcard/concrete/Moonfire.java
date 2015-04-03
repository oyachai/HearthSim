package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;

public class Moonfire extends SpellDamage {

    public Moonfire() {
        super();
    }

    @Deprecated
    public Moonfire(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }
}
