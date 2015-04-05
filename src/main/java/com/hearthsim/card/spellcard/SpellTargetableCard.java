package com.hearthsim.card.spellcard;

import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectOnResolveTargetableInterface;

public abstract class SpellTargetableCard extends SpellCard implements CardEffectOnResolveTargetableInterface {
    // TODO not really needed anymore; refactor subclasses to ditch this
    protected CardEffectCharacter effect;

    @Override
    public CharacterFilter getTargetableFilter() {
        return CharacterFilterTargetedSpell.ALL;
    }
}
