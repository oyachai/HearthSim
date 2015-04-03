package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectHeroBuff;

public class Claw extends SpellTargetableCard {

    private static final byte DAMAGE_AMOUNT = 2;
    private static final byte ARMOR_AMOUNT = 2;

    private static final CardEffectCharacter effect = new CardEffectHeroBuff(DAMAGE_AMOUNT, ARMOR_AMOUNT);

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
    public CharacterFilter getTargetableFilter() {
        return CharacterFilterTargetedSpell.SELF;
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
    public CardEffectCharacter getTargetableEffect() {
        return Claw.effect;
    }
}
