package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectHero;
import com.hearthsim.event.effect.CardEffectHeroMana;

public class ArcaneGolem extends Minion implements MinionBattlecryInterface {

    private final static CardEffectHero<Minion> effect = new CardEffectHeroMana<>(1, 1);

    public ArcaneGolem() {
        super();
    }

    @Override
    public CharacterFilter getBattlecryFilter() {
        return CharacterFilter.OPPONENT;
    }

    @Override
    public CardEffectCharacter<Minion> getBattlecryEffect() {
        return ArcaneGolem.effect;
    }
}
