package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public abstract class CardEffectHero extends CardEffectCharacter {
    public abstract HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, HearthTreeNode boardState) throws HSException;

    @Override
    public final HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
        return this.applyEffect(originSide, origin, targetSide, boardState);
    }
}
