package com.hearthsim.event.effect.conditional;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class EffectCharacterConditional<T extends Card> implements EffectCharacter<T> {

    private final EffectCharacter<T> baseEffect;
    private final Conditional condition;
    private final PlayerSide conditionForSide;

    public EffectCharacterConditional(EffectCharacter<T> baseEffect, Conditional condition, PlayerSide conditionForSide) {
        this.baseEffect = baseEffect;
        this.condition = condition;
        this.conditionForSide = conditionForSide;
    }

    @Override
    public HearthTreeNode applyEffect(PlayerSide targetSide, CharacterIndex targetCharacterIndex, HearthTreeNode boardState) {
        if (condition.isSatisfied(conditionForSide, boardState)) {
            return baseEffect.applyEffect(targetSide, targetCharacterIndex, boardState);
        } else {
            return boardState;
        }
    }
}
