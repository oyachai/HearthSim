package com.hearthsim.event;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class EffectMinionMindControl extends EffectMinionActionUntargetable {
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
}
