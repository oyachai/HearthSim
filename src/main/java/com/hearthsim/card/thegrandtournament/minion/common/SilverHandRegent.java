package com.hearthsim.card.thegrandtournament.minion.common;

import com.hearthsim.card.basic.minion.SilverHandRecruit;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionWithInspire;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterSummonNew;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterInterface;

/**
 * Created by oyachai on 8/17/15.
 */
public class SilverHandRegent extends MinionWithInspire<Minion> {

    private static final EffectCharacter<Minion> effect = new EffectCharacterSummonNew<>(SilverHandRecruit.class);

    @Override
    public FilterCharacterInterface getInspireFilter() {
        return FilterCharacter.ORIGIN;
    }


    public SilverHandRegent() {
        super();
    }

    @Override
    public EffectCharacter<Minion> getInspireEffect() {
        return effect;
    }
}
