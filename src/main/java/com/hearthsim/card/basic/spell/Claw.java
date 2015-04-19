package com.hearthsim.card.basic.spell;

import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectHeroBuff;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;

public class Claw extends SpellTargetableCard {

    private static final byte DAMAGE_AMOUNT = 2;
    private static final byte ARMOR_AMOUNT = 2;

    private static final EffectCharacter effect = new EffectHeroBuff(DAMAGE_AMOUNT, ARMOR_AMOUNT);

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public Claw(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public Claw() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.SELF;
    }

    /**
     * Claw
     *
     * Gives the hero +2 attack for this turn and +2 armor
     *
     *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    public EffectCharacter getTargetableEffect() {
        return Claw.effect;
    }
}
