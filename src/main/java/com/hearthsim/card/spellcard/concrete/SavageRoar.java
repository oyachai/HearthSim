package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuffTemp;
import com.hearthsim.event.effect.CardEffectOnResolveAoeInterface;

public class SavageRoar extends SpellCard implements CardEffectOnResolveAoeInterface {

    private final static CardEffectCharacter effect = new CardEffectCharacterBuffTemp(2);

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public SavageRoar(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Gives all friendly characters +2 attack for this turn
     */
    public SavageRoar() {
        super();
    }

    @Override
    public CardEffectCharacter getAoeEffect() { return SavageRoar.effect; }

    @Override
    public CharacterFilter getAoeFilter() {
        return CharacterFilter.ALL_FRIENDLIES;
    }
}
