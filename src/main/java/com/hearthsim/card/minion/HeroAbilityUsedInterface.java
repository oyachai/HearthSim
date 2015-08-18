package com.hearthsim.card.minion;

import com.hearthsim.card.Card;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterInterface;

@FunctionalInterface
public interface HeroAbilityUsedInterface<T extends Card> {

    public default FilterCharacterInterface getInspireFilter() {
        return FilterCharacter.SELF;
    }

    public EffectCharacter<T> getInspireEffect();

}
