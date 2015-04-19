package com.hearthsim.card.classic.spell.common;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectOnResolveRandomCharacter;
import com.hearthsim.event.filter.FilterCharacter;

public class DeadlyShot extends SpellCard implements EffectOnResolveRandomCharacter {
    @Override
    public EffectCharacter getRandomTargetEffect() {
        return EffectCharacter.DESTROY;
    }

    @Override
    public FilterCharacter getRandomTargetFilter() {
        return FilterCharacter.ENEMY_MINIONS;
    }
}
