package com.hearthsim.event;

import com.hearthsim.card.Card;
import com.hearthsim.model.PlayerSide;

public abstract class EffectMinionAction<T extends Card, U> {
    public abstract U applyEffect(PlayerSide originSide, T origin, PlayerSide targetSide, int targetCharacterIndex, U board);
}
