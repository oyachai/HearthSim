package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamageAoe;

public class Whirlwind extends SpellDamageAoe {

    private static final byte DAMAGE_AMOUNT = 1;

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public Whirlwind(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public Whirlwind() {
        super();

        this.hitsEnemyMinions = true;
        this.hitsOwnMinions = true;
    }
}
