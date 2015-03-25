package com.hearthsim.event;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public abstract class EffectMinionAction<T extends Card> {
    public abstract HearthTreeNode applyEffect(PlayerSide originSide, T origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException;

    public final HearthTreeNode applyEffect(PlayerSide originSide, T origin, PlayerSide targetSide, Minion targetCharacter, HearthTreeNode boardState) throws HSException {
        int index = boardState.data_.modelForSide(targetSide).getIndexForCharacter(targetCharacter);
        return this.applyEffect(originSide, origin, targetSide, index, boardState);
    }

    public final static EffectMinionAction<Card> BOUNCE = new EffectMinionAction<Card>() {
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

    public final static EffectMinionAction<Card> MIND_CONTROL = new EffectMinionAction<Card>() {
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
