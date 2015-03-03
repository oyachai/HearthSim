package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamageAoe;

public class Flamestrike extends SpellDamageAoe {

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public Flamestrike(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public Flamestrike() {
        super();
    }
}
