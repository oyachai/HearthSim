package com.hearthsim.card.goblinsvsgnomes.minion.common;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.ActiveEffectHand;
import com.hearthsim.event.effect.EffectHandManaCost;
import com.hearthsim.event.effect.SimpleEffectHand;
import com.hearthsim.event.filter.FilterHand;

public class Mechwarper extends Minion implements ActiveEffectHand {
    private static final SimpleEffectHand effect = new EffectHandManaCost(-1);
    private static final SimpleEffectHand effectUndo = new EffectHandManaCost(1);
    private static final FilterHand filter = new FilterHand() {

        @Override
        protected boolean includeOwnHand() {
            return true;
        }

        @Override
        protected Class<? extends Card> classFilter() {
            return Minion.class;
        }

        @Override
        protected MinionTribe tribeFilter() {
            return MinionTribe.MECH;
        }
    };

    @Override
    public SimpleEffectHand getActiveEffect() {
        return Mechwarper.effect;
    }

    @Override
    public SimpleEffectHand undoActiveEffect() {
        return Mechwarper.effectUndo;
    }

    @Override
    public FilterHand getActiveFilter() {
        return Mechwarper.filter;
    }
}
