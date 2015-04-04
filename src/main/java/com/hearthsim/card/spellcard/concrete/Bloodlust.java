package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuffTemp;
import com.hearthsim.event.effect.CardEffectOnResolveAoeInterface;

public class Bloodlust extends SpellCard implements CardEffectOnResolveAoeInterface {

    private final static CardEffectCharacter effect = new CardEffectCharacterBuffTemp(3);

    /**
     * Give your minions +3 attack for this turn
     */
    public Bloodlust() {
        super();
    }

    @Deprecated
    public Bloodlust(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    @Override
    public CardEffectCharacter getAoeEffect() { return Bloodlust.effect; }

    @Override
    public CharacterFilter getAoeFilter() {
        return CharacterFilter.FRIENDLY_MINIONS;
    }
}
