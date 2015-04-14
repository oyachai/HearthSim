package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;

public interface EffectOnResolveTargetable<T extends Card> {
    public EffectCharacter<T> getTargetableEffect();
    public default FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.ALL;
    }
}
