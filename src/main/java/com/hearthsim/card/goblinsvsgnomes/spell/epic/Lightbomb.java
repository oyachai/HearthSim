package com.hearthsim.card.goblinsvsgnomes.spell.epic;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterDamageByAttack;
import com.hearthsim.event.effect.EffectOnResolveAoe;
import com.hearthsim.event.filter.FilterCharacter;

public class Lightbomb extends SpellCard implements EffectOnResolveAoe {

    private static final EffectCharacter effect = new EffectCharacterDamageByAttack(true);

    public Lightbomb() {
        super();
    }

    @Override
    public EffectCharacter getAoeEffect() {
        return effect;
    }

    @Override
    public FilterCharacter getAoeFilter() {
        return FilterCharacter.ALL_MINIONS;
    }

}
