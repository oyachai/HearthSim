package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuff;
import com.hearthsim.event.effect.CardEffectOnResolveAoeInterface;

public class Equality extends SpellCard implements CardEffectOnResolveAoeInterface {

    private static final CardEffectCharacter effect = new CardEffectCharacterBuff(0, 1);

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public Equality(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Change the Health of ALL minions to 1
     */
    public Equality() {
        super();
    }

    @Override
    public CardEffectCharacter getAoeEffect() { return Equality.effect; }

    @Override
    public CharacterFilter getAoeFilter() {
        return CharacterFilter.ALL_MINIONS;
    }
}
