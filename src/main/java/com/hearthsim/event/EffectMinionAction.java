package com.hearthsim.event;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public abstract class EffectMinionAction {
    public abstract boolean canEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter);

    public abstract void applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter);

    public void applyEffectToBoard(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, BoardModel board) {
        Minion targetCharacter = board.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        this.applyEffect(originSide, origin, targetSide, targetCharacter);
    }
}
