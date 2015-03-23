package com.hearthsim.event;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class EffectMinionMindControl extends EffectMinionActionUntargetable {
    public void applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, BoardModel board) {
        Minion targetMinion = board.modelForSide(targetSide).getCharacter(targetCharacterIndex);

        board.removeMinion(targetSide, targetCharacterIndex - 1);
        board.placeMinion(originSide, targetMinion);

        if (targetMinion.getCharge()) {
            if (!targetMinion.canAttack()) {
                targetMinion.hasAttacked(false);
            }
        } else {
            targetMinion.hasAttacked(true);
        }
        targetMinion.hasBeenUsed(true);
    }
}
