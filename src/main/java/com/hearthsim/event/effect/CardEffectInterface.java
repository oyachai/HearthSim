package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

@FunctionalInterface
public interface CardEffectInterface<T extends Card, U extends Card> {
    public HearthTreeNode applyEffect(PlayerSide originSide, T origin, PlayerSide targetSide, U target, HearthTreeNode boardState);
}
