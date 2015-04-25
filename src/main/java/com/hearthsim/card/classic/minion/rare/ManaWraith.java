package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.ActiveEffectHand;
import com.hearthsim.event.effect.EffectHandManaCost;
import com.hearthsim.event.effect.SimpleEffectHand;
import com.hearthsim.event.filter.FilterHand;

public class ManaWraith extends Minion implements ActiveEffectHand {
    private static final SimpleEffectHand effect = new EffectHandManaCost(1);
    private static final SimpleEffectHand effectUndo = new EffectHandManaCost(-1);

    @Override
    public SimpleEffectHand getActiveEffect() {
        return ManaWraith.effect;
    }

    @Override
    public SimpleEffectHand undoActiveEffect() {
        return ManaWraith.effectUndo;
    }

    @Override
    public FilterHand getActiveFilter() {
        return FilterHand.ALL_MINIONS;
    }
}
