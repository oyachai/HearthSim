package com.hearthsim.card.blackrockmountain.spell.rare;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterDamageSpell;
import com.hearthsim.event.effect.EffectOnResolveAoe;
import com.hearthsim.event.filter.FilterCharacter;

/**
 * Created by oyachai on 8/23/15.
 */
public class Demonwrath extends SpellCard implements EffectOnResolveAoe {

    private static final EffectCharacter effect = new EffectCharacterDamageSpell<>(2);

    public Demonwrath() {
        super();
    }

    @Override
    public EffectCharacter getAoeEffect() {
        return Demonwrath.effect;
    }

    @Override
    public FilterCharacter getAoeFilter() {
        return FilterCharacter.ALL_NON_DEMONS;
    }
}
