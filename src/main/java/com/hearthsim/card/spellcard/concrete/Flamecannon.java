package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectOnResolveRandomCharacterInterface;

public class Flamecannon extends SpellDamage implements CardEffectOnResolveRandomCharacterInterface {
    @Override
    public CardEffectCharacter getRandomTargetEffect() {
        return this.getSpellDamageEffect();
    }

    @Override
    public FilterCharacter getRandomTargetFilter() {
        return FilterCharacter.ENEMY_MINIONS;
    }
}
