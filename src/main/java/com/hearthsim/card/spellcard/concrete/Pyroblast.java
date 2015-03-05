package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;

public class Pyroblast extends SpellDamage {

    public Pyroblast() {
        super();
    }

    @Deprecated
    public Pyroblast(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    @Override
    public SpellDamage deepCopy() {
        return new Pyroblast(this.hasBeenUsed);
    }
}
