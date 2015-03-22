package com.hearthsim.event;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class EffectMinionBounce extends EffectMinionAction {
    // TODO currently only used by deathrattles so this is hardcoded to anub'ar ambusher's needs. we should make this easier to customize.
    public boolean canEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
        if (targetSide != originSide) {
            return false;
        }

        if (targetCharacter == origin) {
            return false;
        }

        if (targetCharacter.isHero()) {
            return false;
        }

        return true;
    }

    public void applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, BoardModel board) {
        Minion targetCharacter = board.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        if (board.modelForSide(targetSide).getHand().size() < 10) {
            Minion copy = targetCharacter.createResetCopy();
            board.modelForSide(targetSide).placeCardHand(copy);
        }
        board.removeMinion(targetCharacter);
    }
}
