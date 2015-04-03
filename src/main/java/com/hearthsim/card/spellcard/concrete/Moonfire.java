package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamageTargetableCard;

public class Moonfire extends SpellDamageTargetableCard {

    public Moonfire() {
        super();
    }

    @Deprecated
    public Moonfire(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }
}
