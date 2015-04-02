package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuffDelta;

public class Bananas extends SpellCard {

    private final static CardEffectCharacter effect = new CardEffectCharacterBuffDelta(1, 1);

    @Deprecated
    public Bananas(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }


    public Bananas() {
        super();
    }

    @Override
    public CharacterFilter getTargetableFilter() {
        return CharacterFilterTargetedSpell.ALL_MINIONS;
    }

    @Override
    public CardEffectCharacter getTargetableEffect() {
        return Bananas.effect;
    }
}
