package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.event.filter.FilterCharacter;

public interface EffectOnResolveRandomCharacter {

    public EffectCharacter<Card> getRandomTargetEffect();

    public FilterCharacter getRandomTargetFilter();

    public default EffectCharacter<Card> getRandomTargetSecondaryEffect() {
        return null;
    }
}
