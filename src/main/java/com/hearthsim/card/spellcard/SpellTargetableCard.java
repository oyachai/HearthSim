package com.hearthsim.card.spellcard;

import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectOnResolveTargetableInterface;

public abstract class SpellTargetableCard extends SpellCard implements CardEffectOnResolveTargetableInterface {
    // TODO not really needed anymore; refactor subclasses to ditch this
    protected CardEffectCharacter effect;

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.ALL;
    }
}
