package com.hearthsim.card.goblinsvsgnomes.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectHeroWeaponBuffDelta;

public class GoblinAutoBarber extends Minion implements MinionBattlecryInterface {

    private static final EffectCharacter effect = new EffectHeroWeaponBuffDelta<>(1);

    public GoblinAutoBarber() {
        super();
    }

    /**
     * Battlecry: Draw one card
     */
    @Override
    public EffectCharacter getBattlecryEffect() {
        return GoblinAutoBarber.effect;
    }
}
