package com.hearthsim.card.goblinsvsgnomes.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterHeal;

public class AntiqueHealbot extends Minion implements MinionBattlecryInterface {

    private static final EffectCharacter effect = new EffectCharacterHeal(8);

    public AntiqueHealbot() {
        super();
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return AntiqueHealbot.effect;
    }
}
