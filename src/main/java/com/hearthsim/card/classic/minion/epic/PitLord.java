package com.hearthsim.card.classic.minion.epic;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterDamage;

public class PitLord extends Minion implements MinionBattlecryInterface {

    private final static EffectCharacter effect = new EffectCharacterDamage(5);


    public PitLord() {
        super();
    }

    /**
     * Battlecry: Deal 5 damage to your hero
     */
    @Override
    public EffectCharacter getBattlecryEffect() {
        return PitLord.effect;
    }
}
