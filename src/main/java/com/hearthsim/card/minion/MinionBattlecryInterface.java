package com.hearthsim.card.minion;

import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterInterface;
import com.hearthsim.event.effect.CardEffectCharacter;

@FunctionalInterface
public interface MinionBattlecryInterface {
    public default CharacterFilterInterface getBattlecryFilter() {
        return CharacterFilter.SELF;
    }

    public CardEffectCharacter<Minion> getBattlecryEffect();
}
