package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamageTargetableCard;

public class Pyroblast extends SpellDamageTargetableCard {

    public Pyroblast() {
        super();
    }

    @Deprecated
    public Pyroblast(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }
}
