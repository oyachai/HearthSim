package com.hearthsim.event.effect;

import com.hearthsim.event.HandFilter;

public interface CardEffectOnResolveRandomHandInterface extends CardEffectOnResolveRandomInterface<CardEffectHand, HandFilter> {
    @Override
    public CardEffectHand getRandomTargetEffect();

    @Override
    public HandFilter getRandomTargetFilter();
}
