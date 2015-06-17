package com.hearthsim.event.effect;

import com.hearthsim.event.filter.FilterHand;

public interface EffectOnResolveRandomHand {

    public EffectHand getRandomTargetEffect();

    public FilterHand getRandomTargetFilter();

    public default EffectHand getRandomTargetSecondaryEffect() {
        return null;
    }

}
