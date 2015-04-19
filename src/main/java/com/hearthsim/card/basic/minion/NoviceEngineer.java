package com.hearthsim.card.basic.minion;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectHeroDraw;

public class NoviceEngineer extends Minion implements MinionBattlecryInterface {

    private static final EffectCharacter<Minion> effect = new EffectHeroDraw<>(1);

    public NoviceEngineer() {
        super();
    }

    /**
     * Battlecry: Draw one card
     */
    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return NoviceEngineer.effect;
    }
}
