package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

@FunctionalInterface
public interface EffectCharacter<T extends Card> extends EffectInterface<T> {
    @Override
    public HearthTreeNode applyEffect(PlayerSide originSide, T origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState);

    public default HearthTreeNode applyEffect(PlayerSide originSide, T origin, PlayerSide targetSide, Card targetCharacter, HearthTreeNode boardState) {
        // MrHen: I have no idea why this (U) conversion is necessary but I get a weird compile assert if I set up the generics to use CardEffectInterface<T, Minion>
        int index = boardState.data_.modelForSide(targetSide).getIndexForCharacter((Minion)targetCharacter);
        return this.applyEffect(originSide, origin, targetSide, index, boardState);
    }

    public final static EffectCharacter BOUNCE = (originSide, origin, targetSide, targetCharacterIndex, boardState) -> {
        Minion targetCharacter = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        if (boardState.data_.modelForSide(targetSide).getHand().size() < 10) {
            Minion copy = (Minion)targetCharacter.createResetCopy();
            boardState.data_.modelForSide(targetSide).placeCardHand(copy);
            boardState.data_.removeMinion(targetCharacter);
        } else {
            targetCharacter.setHealth((byte) -99);
        }
        return boardState;
    };

    public final static EffectCharacter<Card> DESTROY = (originSide, origin, targetSide, targetCharacterIndex, boardState) -> {
        Minion targetCharacter = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        targetCharacter.setHealth((byte) -99);
        return boardState;
    };

    public final static EffectCharacter FREEZE = (originSide, origin, targetSide, targetCharacterIndex, boardState) -> {
        Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        targetMinion.setFrozen(true);
        return boardState;
    };

    public final static EffectCharacter SILENCE = (originSide, origin, targetSide, targetCharacterIndex, boardState) -> {
        Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        targetMinion.silenced(targetSide, boardState.data_);
        return boardState;
    };

    public final static EffectCharacter MIND_CONTROL = (originSide, origin, targetSide, targetCharacterIndex, boardState) -> {
        Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);

        boardState.data_.removeMinion(targetSide, targetCharacterIndex - 1);
        boardState.data_.placeMinion(originSide, targetMinion);

        if (targetMinion.getCharge()) {
            if (!targetMinion.canAttack()) {
                targetMinion.hasAttacked(false);
            }
        } else {
            targetMinion.hasAttacked(true);
        }
        targetMinion.hasBeenUsed(true);
        return boardState;
    };
}
