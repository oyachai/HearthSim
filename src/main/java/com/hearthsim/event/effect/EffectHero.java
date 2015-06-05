package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public interface EffectHero<T extends Card> extends EffectCharacter<T> {
    public HearthTreeNode applyEffect(PlayerSide originSide, T origin, PlayerSide targetSide, HearthTreeNode boardState);

    @Override
    public default HearthTreeNode applyEffect(PlayerSide originSide, T origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
        return this.applyEffect(originSide, origin, targetSide, boardState);
    }
}
