package com.hearthsim.card.basic.minion;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffDelta;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;

public class ShatteredSunCleric extends Minion implements MinionBattlecryInterface {
    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    private final static EffectCharacter battlecryAction = new EffectCharacterBuffDelta(1, 1);

    public ShatteredSunCleric() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return ShatteredSunCleric.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return ShatteredSunCleric.battlecryAction;
    }
}
