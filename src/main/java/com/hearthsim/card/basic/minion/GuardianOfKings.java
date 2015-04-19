package com.hearthsim.card.basic.minion;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterHeal;

public class GuardianOfKings extends Minion implements MinionBattlecryInterface {

    private static final EffectCharacter effect = new EffectCharacterHeal(6);

    public GuardianOfKings() {
        super();
    }

    /**
     * Battlecry: Restore 6 Health to your Hero
     */
    @Override
    public EffectCharacter getBattlecryEffect() {
        return GuardianOfKings.effect;
    }
}
