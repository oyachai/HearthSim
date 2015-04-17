package com.hearthsim.card.spellcard;

import com.hearthsim.event.effect.EffectCharacterDamageSpell;
import com.hearthsim.event.effect.EffectOnResolveTargetable;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;

public abstract class SpellDamageTargetableCard extends SpellDamage implements EffectOnResolveTargetable<SpellDamage> {
    protected EffectCharacterDamageSpell<SpellDamage> effect;

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.ALL;
    }

    // damage is set during card import so we need to lazy load this for each card
    @Override
    public EffectCharacterDamageSpell<SpellDamage> getTargetableEffect() {
        return this.getSpellDamageEffect();
    }
}
