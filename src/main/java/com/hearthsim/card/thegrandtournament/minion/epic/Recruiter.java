package com.hearthsim.card.thegrandtournament.minion.epic;

import com.hearthsim.card.classic.minion.common.Squire;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionWithInspire;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectHeroAddCardHand;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterInterface;

/**
 * Created by oyachai on 8/14/15.
 */
public class Recruiter extends MinionWithInspire<Minion> {

    private static final EffectCharacter<Minion> effect = new EffectHeroAddCardHand<>(Squire.class);

    @Override
    public FilterCharacterInterface getInspireFilter() {
        return FilterCharacter.ORIGIN;
    }


    public Recruiter() {
        super();
    }

    @Override
    public EffectCharacter<Minion> getInspireEffect() {
        return effect;
    }
}
