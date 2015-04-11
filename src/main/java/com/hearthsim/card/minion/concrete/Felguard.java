package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectHero;
import com.hearthsim.event.effect.CardEffectHeroMana;

public class Felguard extends Minion implements MinionBattlecryInterface {

    private final static CardEffectHero<Minion> effect = new CardEffectHeroMana<>(0, -1);

    public Felguard() {
        super();
    }

    /**
     * Taunt.  Battlecry: Destroy one of your Mana Crystals
     */
    @Override
    public CardEffectCharacter<Minion> getBattlecryEffect() {
        return Felguard.effect;
    }
}
