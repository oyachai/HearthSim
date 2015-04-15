package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.event.filter.FilterCharacter;

public interface EffectOnResolveRandomCharacter extends EffectOnResolveRandom<EffectCharacter<Card>, FilterCharacter> {
    @Override
    public EffectCharacter<Card> getRandomTargetEffect();

    @Override
    public FilterCharacter getRandomTargetFilter();
}
