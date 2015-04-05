package com.hearthsim.card.spellcard;

import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectOnResolveTargetableInterface;
import com.hearthsim.event.effect.SpellEffectCharacterDamage;

public abstract class SpellDamageTargetableCard extends SpellDamage implements CardEffectOnResolveTargetableInterface {
    protected SpellEffectCharacterDamage effect;

    @Override
    public CharacterFilter getTargetableFilter() {
        return CharacterFilterTargetedSpell.ALL;
    }

    // damage is set during card import so we need to lazy load this for each card
    @Override
    public SpellEffectCharacterDamage getTargetableEffect() {
        return this.getSpellDamageEffect();
    }
}
