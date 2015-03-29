package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellAoeInterface;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.card.spellcard.SpellDamageAoe;
import com.hearthsim.event.MinionFilter;
import com.hearthsim.event.MinionFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacter;

public class Hellfire extends SpellDamage implements SpellAoeInterface {

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
        this.minionFilter = MinionFilterTargetedSpell.OPPONENT;
    }

    @Override
    public CardEffectCharacter getAoeEffect() { return this.getEffect(); }

    @Override
    public MinionFilter getAoeFilter() {
        return MinionFilter.ALL;
    }
}
