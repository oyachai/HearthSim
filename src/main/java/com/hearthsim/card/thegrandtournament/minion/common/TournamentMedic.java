package com.hearthsim.card.thegrandtournament.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionWithInspire;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterHeal;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterInterface;

/**
 * Created by oyachai on 8/17/15.
 */
public class TournamentMedic extends MinionWithInspire<Minion> {

    private static final EffectCharacter<Minion> effect = new EffectCharacterHeal<>(2);

    @Override
    public FilterCharacterInterface getInspireFilter() {
        return FilterCharacter.SELF;
    }


    public TournamentMedic() {
        super();
    }

    @Override
    public EffectCharacter<Minion> getInspireEffect() {
        return effect;
    }
}
