package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectRandomTargetInterface;

public class Flamecannon extends SpellDamage implements CardEffectRandomTargetInterface {

    @Override
    public CharacterFilter getTargetableFilter() {
        return CharacterFilterTargetedSpell.OPPONENT;
    }

    @Override
    public CardEffectCharacter getRandomTargetEffect() {
        return this.effect;
    }

    @Override
    public CharacterFilter getRandomTargetFilter() {
        return CharacterFilter.ENEMY_MINIONS;
    }
}
