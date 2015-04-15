package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectOnResolveRandomCharacter;

public class Brawl extends SpellCard implements EffectOnResolveRandomCharacter {
    @Override
    public EffectCharacter getRandomTargetEffect() {
        return null;
    }

    @Override
    public EffectCharacter getRandomTargetSecondaryEffect() {
        return EffectCharacter.DESTROY;
    }

    @Override
    public FilterCharacter getRandomTargetFilter() {
        return FilterCharacter.ALL_MINIONS;
    }
}
