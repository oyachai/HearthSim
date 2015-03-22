package com.hearthsim.event;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class EffectMinionMindControl extends EffectMinionAction {
    public boolean canEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
        if (originSide == targetSide) {
            return false;
        }

        if (targetCharacter.isHero()) {
            return false;
        }

        if (!targetCharacter.isAlive()) {
            return false;
        }

        return true;
    }

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
