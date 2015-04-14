package com.hearthsim.event.effect;

import com.hearthsim.event.filter.FilterHand;

public interface CardEffectOnResolveRandomHandInterface extends CardEffectOnResolveRandomInterface<CardEffectHand, FilterHand> {
    @Override
    public CardEffectHand getRandomTargetEffect();

    @Override
    public FilterHand getRandomTargetFilter();
}
