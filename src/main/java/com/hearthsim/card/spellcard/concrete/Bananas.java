package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
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
    protected CharacterFilter getTargetFilter() {
        return CharacterFilterTargetedSpell.ALL_MINIONS;
    }

    @Override
    protected CardEffectCharacter getEffect() {
        return Bananas.effect;
    }
}
