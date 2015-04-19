package com.hearthsim.card.basic.minion;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectHeroDraw;

public class GnomishInventor extends Minion implements MinionBattlecryInterface {

    private static final EffectCharacter effect = new EffectHeroDraw(1);

    public GnomishInventor() {
        super();
    }

    /**
     * Battlecry: Draw one card
     */
    @Override
    public EffectCharacter getBattlecryEffect() {
        return GnomishInventor.effect;
    }
}
