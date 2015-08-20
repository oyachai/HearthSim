package com.hearthsim.card.blackrockmountain.spell.rare;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterDamageRevenge;
import com.hearthsim.event.effect.EffectOnResolveAoe;
import com.hearthsim.event.filter.FilterCharacter;

/**
 * Created by oyachai on 8/20/15.
 */
public class Revenge extends SpellCard implements EffectOnResolveAoe {

    protected EffectCharacterDamageRevenge effect;

    public Revenge() {
        super();
    }

    @Override
    public EffectCharacter getAoeEffect() {
        if (effect == null)
            effect = new EffectCharacterDamageRevenge();
        return this.effect;
    }

    @Override
    public FilterCharacter getAoeFilter() {
        return FilterCharacter.ALL_MINIONS;
    }

}
