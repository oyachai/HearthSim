package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectAoeInterface;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterHeal;

public class CircleOfHealing extends SpellCard implements CardEffectAoeInterface {

    private static final byte HEAL_AMOUNT = 4;

    private static final CardEffectCharacter effect = new CardEffectCharacterHeal(CircleOfHealing.HEAL_AMOUNT);

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public CircleOfHealing() {
        super();
    }

    @Override
    public CharacterFilter getTargetableFilter() {
        return CharacterFilterTargetedSpell.SELF;
    }

    /**
     *
     * Circle of Healing
     *
     * Heals all minions for 4
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
        return this.getAoeEffect();
    }

    @Override
    public CardEffectCharacter getAoeEffect() {
        return CircleOfHealing.effect;
    }

    @Override
    public CharacterFilter getAoeFilter() {
        return CharacterFilter.ALL_MINIONS;
    }
}
