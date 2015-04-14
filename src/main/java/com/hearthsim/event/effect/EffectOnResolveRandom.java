package com.hearthsim.event.effect;

import com.hearthsim.event.filter.FilterInterface;

public interface EffectOnResolveRandom<T extends EffectInterface, U extends FilterInterface> {
    public T getRandomTargetEffect();

    public default T getRandomTargetSecondaryEffect() {
        return null;
    }

    public U getRandomTargetFilter();
}
