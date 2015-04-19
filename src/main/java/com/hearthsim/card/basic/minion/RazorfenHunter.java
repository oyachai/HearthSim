package com.hearthsim.card.basic.minion;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterSummon;

public class RazorfenHunter extends Minion implements MinionBattlecryInterface {

    public RazorfenHunter() {
        super();
    }

    /**
     * Battlecry: Summon a 1/1 Boar
     */
    @Override
    public EffectCharacter getBattlecryEffect() {
        return new EffectCharacterSummon(new Boar());
    }
}
