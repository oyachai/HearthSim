package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectOnResolveAoeInterface;

public class TwistingNether extends SpellCard implements CardEffectOnResolveAoeInterface {

    public TwistingNether() {
        super();
    }

    @Override
    public CardEffectCharacter getAoeEffect() { return CardEffectCharacter.DESTROY; }

    @Override
    public CharacterFilter getAoeFilter() {
        return CharacterFilter.ALL_MINIONS;
    }
}
