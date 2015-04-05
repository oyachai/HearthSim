package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectOnResolveRandomCharacterInterface;

public class Flamecannon extends SpellDamage implements CardEffectOnResolveRandomCharacterInterface {
    @Override
    public CardEffectCharacter getRandomTargetEffect() {
        return this.getSpellDamageEffect();
    }

    @Override
    public CardEffectCharacter getRandomTargetSecondaryEffect() {
        return null;
    }

    @Override
    public CharacterFilter getRandomTargetFilter() {
        return CharacterFilter.ENEMY_MINIONS;
    }
}
