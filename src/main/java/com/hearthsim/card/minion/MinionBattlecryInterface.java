package com.hearthsim.card.minion;

import com.hearthsim.card.Card;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterInterface;
import com.hearthsim.event.effect.CardEffectCharacter;

@FunctionalInterface
public interface MinionBattlecryInterface<T extends Card> {
    public default CharacterFilterInterface getBattlecryFilter() {
        return CharacterFilter.SELF;
    }

    public CardEffectCharacter<T> getBattlecryEffect();
}
