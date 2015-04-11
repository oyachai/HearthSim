package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;

public interface CardEffectOnResolveTargetableInterface<T extends Card> {
    public CardEffectCharacter<T> getTargetableEffect();
    public default CharacterFilter getTargetableFilter() {
        return CharacterFilterTargetedSpell.ALL;
    }
}
