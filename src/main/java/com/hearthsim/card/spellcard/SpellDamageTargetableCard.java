package com.hearthsim.card.spellcard;

import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.event.effect.CardEffectOnResolveTargetableInterface;
import com.hearthsim.event.effect.SpellEffectCharacterDamage;

public abstract class SpellDamageTargetableCard extends SpellDamage implements CardEffectOnResolveTargetableInterface<SpellDamage> {
    protected SpellEffectCharacterDamage<SpellDamage> effect;

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.ALL;
    }

    // damage is set during card import so we need to lazy load this for each card
    @Override
    public SpellEffectCharacterDamage<SpellDamage> getTargetableEffect() {
        return this.getSpellDamageEffect();
    }
}
