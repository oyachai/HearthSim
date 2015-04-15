package com.hearthsim.event.effect;

import com.hearthsim.event.filter.FilterHand;

public interface EffectOnResolveRandomHand extends EffectOnResolveRandom<EffectHand, FilterHand> {
    @Override
    public EffectHand getRandomTargetEffect();

    @Override
    public FilterHand getRandomTargetFilter();
}
