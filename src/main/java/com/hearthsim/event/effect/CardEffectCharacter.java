package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public abstract class CardEffectCharacter {
    public abstract HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState);

    public final HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, HearthTreeNode boardState) {
        int index = boardState.data_.modelForSide(targetSide).getIndexForCharacter(targetCharacter);
        return this.applyEffect(originSide, origin, targetSide, index, boardState);
    }

    public final static CardEffectCharacter BOUNCE = new CardEffectCharacter() {
        @Override
        public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
            Minion targetCharacter = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
            if (boardState.data_.modelForSide(targetSide).getHand().size() < 10) {
                Minion copy = targetCharacter.createResetCopy();
                boardState.data_.modelForSide(targetSide).placeCardHand(copy);
                boardState.data_.removeMinion(targetCharacter);
            } else {
                targetCharacter.setHealth((byte) -99);
            }
            return boardState;
        }
    };

    public final static CardEffectCharacter DESTROY = new CardEffectCharacter() {
        @Override
        public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
            Minion targetCharacter = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
            targetCharacter.setHealth((byte) -99);
            return boardState;
        }
    };

    public final static CardEffectCharacter FREEZE = new CardEffectCharacter() {
        @Override
        public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
            Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
            targetMinion.setFrozen(true);
            return boardState;
        }
    };

    public final static CardEffectCharacter SILENCE = new CardEffectCharacter() {
        public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
            Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
            targetMinion.silenced(targetSide, boardState.data_);
            return boardState;
        }
    };

    public final static CardEffectCharacter MIND_CONTROL = new CardEffectCharacter() {
        @Override
        public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
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
        }
    };
}
