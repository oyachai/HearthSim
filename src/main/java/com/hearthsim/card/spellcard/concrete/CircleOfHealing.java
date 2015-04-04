package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterHeal;
import com.hearthsim.event.effect.CardEffectOnResolveAoeInterface;

public class CircleOfHealing extends SpellCard implements CardEffectOnResolveAoeInterface {

    private static final byte HEAL_AMOUNT = 4;

    private static final CardEffectCharacter effect = new CardEffectCharacterHeal(CircleOfHealing.HEAL_AMOUNT);

    /**
     * Heals all minions for 4
     */
    public CircleOfHealing() {
        super();
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
