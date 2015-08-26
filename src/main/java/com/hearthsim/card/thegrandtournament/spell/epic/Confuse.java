package com.hearthsim.card.thegrandtournament.spell.epic;

import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectOnResolveAoe;
import com.hearthsim.event.filter.FilterCharacter;

/**
 * Created by oyachai on 8/23/15.
 */
public class Confuse extends SpellCard implements EffectOnResolveAoe {

    public Confuse() {
        super();
    }

    @Override
    public EffectCharacter getAoeEffect() {
        return EffectCharacter.SWAP_ATTACK_HEALTH;
    }

    @Override
    public FilterCharacter getAoeFilter() {
        return FilterCharacter.ALL_MINIONS;
    }
}
