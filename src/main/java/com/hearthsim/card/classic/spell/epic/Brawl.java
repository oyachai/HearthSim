package com.hearthsim.card.classic.spell.epic;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectOnResolveRandomCharacter;
import com.hearthsim.event.filter.FilterCharacter;

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
