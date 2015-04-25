package com.hearthsim.card.curseofnaxxramas.minion.common;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.ActiveEffectHand;
import com.hearthsim.event.effect.EffectHandManaCost;
import com.hearthsim.event.effect.SimpleEffectHand;
import com.hearthsim.event.filter.FilterHand;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class NerubarWeblord extends Minion implements ActiveEffectHand {
    private static final SimpleEffectHand effect = new EffectHandManaCost(2);
    private static final SimpleEffectHand effectUndo = new EffectHandManaCost(-2);
    private static final FilterHand filter = new FilterHand() {
        @Override
        protected boolean includeEnemyHand() {
            return true;
        }

        @Override
        protected boolean includeOwnHand() {
            return true;
        }

        @Override
        protected Class<? extends Card> classFilter() {
            return Minion.class;
        }

        @Override
        public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Card targetCard, BoardModel board) {
            if (!super.targetMatches(originSide, origin, targetSide, targetCard, board)) {
                return false;
            }

            if (!(targetCard instanceof MinionBattlecryInterface)) {
                return false;
            }

            return true;
        }
    };

    @Override
    public SimpleEffectHand getActiveEffect() {
        return NerubarWeblord.effect;
    }

    @Override
    public SimpleEffectHand undoActiveEffect() {
        return NerubarWeblord.effectUndo;
    }

    @Override
    public FilterHand getActiveFilter() {
        return NerubarWeblord.filter;
    }
}
