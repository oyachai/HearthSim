package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterHeal;

public class PriestessOfElune extends Minion implements MinionBattlecryInterface {

    private static final EffectCharacter effect = new EffectCharacterHeal(4);

    public PriestessOfElune() {
        super();
    }

    /**
     * Battlecry: Restore 4 Health to your Hero
     */
    @Override
    public EffectCharacter getBattlecryEffect() {
        return PriestessOfElune.effect;
    }
}
