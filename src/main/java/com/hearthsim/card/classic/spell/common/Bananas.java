package com.hearthsim.card.classic.spell.common;

import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffDelta;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;

public class Bananas extends SpellTargetableCard {

    private final static EffectCharacter effect = new EffectCharacterBuffDelta(1, 1);

    @Deprecated
    public Bananas(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }


    public Bananas() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.ALL_MINIONS;
    }

    @Override
    public EffectCharacter getTargetableEffect() {
        return Bananas.effect;
    }
}
