package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectHero;
import com.hearthsim.event.effect.EffectHeroMana;

public class Felguard extends Minion implements MinionBattlecryInterface {

    private final static EffectHero<Minion> effect = new EffectHeroMana<>(0, -1);

    public Felguard() {
        super();
    }

    /**
     * Taunt.  Battlecry: Destroy one of your Mana Crystals
     */
    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return Felguard.effect;
    }
}
