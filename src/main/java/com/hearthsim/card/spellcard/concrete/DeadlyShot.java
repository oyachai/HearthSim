package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectOnResolveRandomCharacterInterface;

public class DeadlyShot extends SpellCard implements CardEffectOnResolveRandomCharacterInterface {
    @Override
    public CardEffectCharacter getRandomTargetEffect() {
        return CardEffectCharacter.DESTROY;
    }

    @Override
    public CardEffectCharacter getRandomTargetSecondaryEffect() {
        return null;
    }

    @Override
    public CharacterFilter getRandomTargetFilter() {
        return CharacterFilter.ENEMY_MINIONS;
    }
}
