package com.hearthsim.card.weapon.concrete;

import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuffDelta;
import com.hearthsim.event.effect.CardEffectOnResolveRandomCharacterInterface;

public class Glaivezooka extends WeaponCard implements CardEffectOnResolveRandomCharacterInterface {
    private static final CardEffectCharacter effect = new CardEffectCharacterBuffDelta(1, 0);

    @Override
    public CardEffectCharacter getRandomTargetEffect() {
        return Glaivezooka.effect;
    }

    @Override
    public CardEffectCharacter getRandomTargetSecondaryEffect() {
        return null;
    }

    @Override
    public CharacterFilter getRandomTargetFilter() {
        return CharacterFilter.FRIENDLY_MINIONS;
    }
}
