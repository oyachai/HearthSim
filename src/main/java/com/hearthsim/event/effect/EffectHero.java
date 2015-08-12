package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public interface EffectHero<T extends Card> extends EffectCharacter<T> {
    public HearthTreeNode applyEffect(PlayerSide targetSide, HearthTreeNode boardState);

    @Override
    public default HearthTreeNode applyEffect(PlayerSide targetSide, CharacterIndex targetCharacterIndex, HearthTreeNode boardState) {
        return this.applyEffect(targetSide, boardState);
    }
}
