package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectHeroDraw;

public class AzureDrake extends Minion implements MinionBattlecryInterface {

    private static final EffectCharacter effect = new EffectHeroDraw(1);

    public AzureDrake() {
        super();
    }

    /**
     * Battlecry: Draw one card
     */
    @Override
    public EffectCharacter getBattlecryEffect() {
        return AzureDrake.effect;
    }
}
