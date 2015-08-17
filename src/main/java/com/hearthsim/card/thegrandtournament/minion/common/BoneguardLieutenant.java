package com.hearthsim.card.thegrandtournament.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionWithInspire;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffDelta;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterInterface;

/**
 * Created by oyachai on 8/17/15.
 */
public class BoneguardLieutenant extends MinionWithInspire<Minion> {

    private static final EffectCharacter<Minion> effect = new EffectCharacterBuffDelta<>(0, 1);

    @Override
    public FilterCharacterInterface getInspireFilter() {
        return FilterCharacter.ORIGIN;
    }


    public BoneguardLieutenant() {
        super();
    }

    @Override
    public EffectCharacter<Minion> getInspireEffect() {
        return effect;
    }
}
