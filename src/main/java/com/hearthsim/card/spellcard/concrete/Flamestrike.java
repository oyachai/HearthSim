package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellAoeInterface;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.card.spellcard.SpellDamageAoe;
import com.hearthsim.event.MinionFilter;
import com.hearthsim.event.MinionFilterTargetedSpell;

public class Flamestrike extends SpellDamage implements SpellAoeInterface {

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
        this.minionFilter = MinionFilterTargetedSpell.OPPONENT;
    }

    @Override
    public MinionFilter getHitsFilter() {
        return MinionFilter.ENEMY_MINIONS;
    }
}
