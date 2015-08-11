package com.hearthsim.card.goblinsvsgnomes.spell.epic;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterHealFull;
import com.hearthsim.event.effect.EffectOnResolveAoe;
import com.hearthsim.event.filter.FilterCharacter;

/**
 * Created by oyachai on 8/10/15.
 */
public class TreeOfLife extends SpellCard implements EffectOnResolveAoe {

    private static final EffectCharacter effect = new EffectCharacterHealFull();

    public TreeOfLife() {
        super();
    }

    @Override
    public EffectCharacter getAoeEffect() {
        return TreeOfLife.effect;
    }

    @Override
    public FilterCharacter getAoeFilter() {
        return FilterCharacter.ALL;
    }
}
