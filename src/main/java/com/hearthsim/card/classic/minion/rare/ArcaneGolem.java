package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectHero;
import com.hearthsim.event.effect.EffectHeroMana;

public class ArcaneGolem extends Minion implements MinionBattlecryInterface {

    private final static EffectHero<Minion> effect = new EffectHeroMana<>(1, 1);

    public ArcaneGolem() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return FilterCharacter.OPPONENT;
    }

    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return ArcaneGolem.effect;
    }
}
