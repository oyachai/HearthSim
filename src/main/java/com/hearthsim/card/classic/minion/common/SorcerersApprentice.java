package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.ActiveEffectHand;
import com.hearthsim.event.effect.EffectHandManaCost;
import com.hearthsim.event.effect.SimpleEffectHand;
import com.hearthsim.event.filter.FilterHand;

public class SorcerersApprentice extends Minion implements ActiveEffectHand {
    private static final SimpleEffectHand effect = new EffectHandManaCost(-1);
    private static final SimpleEffectHand effectUndo = new EffectHandManaCost(1);

    @Override
    public SimpleEffectHand getActiveEffect() {
        return SorcerersApprentice.effect;
    }

    @Override
    public SimpleEffectHand undoActiveEffect() {
        return SorcerersApprentice.effectUndo;
    }

    @Override
    public FilterHand getActiveFilter() {
        return FilterHand.FRIENDLY_SPELLS;
    }
}
