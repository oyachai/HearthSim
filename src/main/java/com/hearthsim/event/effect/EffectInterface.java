package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

@FunctionalInterface
public interface EffectInterface<T extends Card> {
    public HearthTreeNode applyEffect(PlayerSide originSide, T origin, PlayerSide targetSide, int targetIndex, HearthTreeNode boardState);
}
