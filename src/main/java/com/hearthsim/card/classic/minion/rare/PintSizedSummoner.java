package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardPlayAfterInterface;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.ActiveEffectHand;
import com.hearthsim.event.effect.EffectHandManaCost;
import com.hearthsim.event.effect.SimpleEffectHand;
import com.hearthsim.event.filter.FilterHand;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class PintSizedSummoner extends Minion implements ActiveEffectHand, CardPlayAfterInterface {
    private static final SimpleEffectHand effect = new EffectHandManaCost(-1);
    private static final SimpleEffectHand effectUndo = new EffectHandManaCost(1);
    private boolean isActive = true;

    @Override
    public boolean isActive(PlayerSide originSide, Card origin, BoardModel board) {
        if (!ActiveEffectHand.super.isActive(originSide, origin, board)) {
            return false;
        }

        return this.isActive;
    }

    @Override
    public SimpleEffectHand getActiveEffect() {
        return PintSizedSummoner.effect;
    }

    @Override
    public SimpleEffectHand undoActiveEffect() {
        return PintSizedSummoner.effectUndo;
    }

    @Override
    public FilterHand getActiveFilter() {
        return FilterHand.FRIENDLY_MINIONS;
    }

    @Override
    public HearthTreeNode startTurn(PlayerSide thisMinionPlayerSide, HearthTreeNode boardState) throws HSException {
        if (!this.isActive) {
            this.isActive = true;
            boardState.data_.applyAuraOfMinion(thisMinionPlayerSide, this);
        }
        return super.startTurn(thisMinionPlayerSide, boardState);
    }

    @Override
    public HearthTreeNode onCardPlayResolve(PlayerSide thisCardPlayerSide, PlayerSide cardUserPlayerSide, Card usedCard, HearthTreeNode boardState) {
        if (usedCard instanceof Minion && thisCardPlayerSide == cardUserPlayerSide && this.isActive) {
            boardState.data_.removeAuraOfMinion(thisCardPlayerSide, this);
            this.isActive = false; // remove aura checks this
        }
        return boardState;
    }
}
