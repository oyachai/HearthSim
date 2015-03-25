package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamageAoe;
import com.hearthsim.event.MinionFilter;

public class Consecration extends SpellDamageAoe {

    private static final byte DAMAGE_AMOUNT = 2;

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public Consecration(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public Consecration() {
        super();
        this.hitsFilter = MinionFilter.ALL_ENEMIES;
    }
}
