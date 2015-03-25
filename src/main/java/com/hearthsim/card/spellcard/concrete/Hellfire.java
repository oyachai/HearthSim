package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamageAoe;
import com.hearthsim.event.MinionFilter;

public class Hellfire extends SpellDamageAoe {

    private static final byte DAMAGE_AMOUNT = 3;

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public Hellfire(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public Hellfire() {
        super();
        this.hitsFilter = MinionFilter.ALL;
    }
}
