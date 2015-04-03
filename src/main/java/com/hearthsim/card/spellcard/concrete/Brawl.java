package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectOnResolveRandomCharacterInterface;

public class Brawl extends SpellCard implements CardEffectOnResolveRandomCharacterInterface {
    @Override
    public CardEffectCharacter getRandomTargetEffect() {
        return null;
    }

    @Override
    public CardEffectCharacter getRandomTargetSecondaryEffect() {
        return CardEffectCharacter.DESTROY;
    }

    @Override
    public CharacterFilter getRandomTargetFilter() {
        return CharacterFilter.ALL_MINIONS;
    }
}
