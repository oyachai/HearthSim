package com.hearthsim.event.effect;

import com.hearthsim.event.CharacterFilter;

public interface CardEffectOnResolveRandomCharacterInterface {
    public CardEffectCharacter getRandomTargetEffect();

    public default CardEffectCharacter getRandomTargetSecondaryEffect() {
        return null;
    }

    public CharacterFilter getRandomTargetFilter();
}
