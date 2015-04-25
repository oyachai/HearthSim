package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.ActiveEffectHand;
import com.hearthsim.event.effect.EffectHandManaCost;
import com.hearthsim.event.effect.SimpleEffectHand;
import com.hearthsim.event.filter.FilterHand;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class VentureCoMercenary extends Minion implements ActiveEffectHand {
    private static final SimpleEffectHand effect = new EffectHandManaCost(3);
    private static final SimpleEffectHand effectUndo = new EffectHandManaCost(-3);

    @Override
    public boolean isActive(PlayerSide originSide, Card origin, BoardModel board) {
        return !origin.isInHand();
    }

    @Override
    public SimpleEffectHand getActiveEffect() {
        return VentureCoMercenary.effect;
    }

    @Override
    public SimpleEffectHand undoActiveEffect() {
        return VentureCoMercenary.effectUndo;
    }

    @Override
    public FilterHand getActiveFilter() {
        return FilterHand.FRIENDLY_MINIONS;
    }
}
