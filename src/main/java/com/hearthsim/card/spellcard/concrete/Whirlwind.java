package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellAoeInterface;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.card.spellcard.SpellDamageAoe;
import com.hearthsim.event.MinionFilter;
import com.hearthsim.event.MinionFilterTargetedSpell;

public class Whirlwind extends SpellDamage implements SpellAoeInterface {

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
        this.minionFilter = MinionFilterTargetedSpell.OPPONENT;
    }

    @Override
    public MinionFilter getHitsFilter() {
        return MinionFilter.ALL_MINIONS;
    }
}
