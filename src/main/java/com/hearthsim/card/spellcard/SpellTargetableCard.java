package com.hearthsim.card.spellcard;

import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectOnResolveTargetable;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;

public abstract class SpellTargetableCard extends SpellCard implements EffectOnResolveTargetable {
    // TODO not really needed anymore; refactor subclasses to ditch this
    protected EffectCharacter effect;

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.ALL;
    }
}
