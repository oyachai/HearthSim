package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.event.effect.CardEffectAoeInterface;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.event.MinionFilter;
import com.hearthsim.event.MinionFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacter;

public class Consecration extends SpellDamage implements CardEffectAoeInterface {

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
        this.minionFilter = MinionFilterTargetedSpell.OPPONENT;
    }

    @Override
    public CardEffectCharacter getAoeEffect() { return this.getEffect(); }

    @Override
    public MinionFilter getAoeFilter() {
        return MinionFilter.ALL_ENEMIES;
    }
}
