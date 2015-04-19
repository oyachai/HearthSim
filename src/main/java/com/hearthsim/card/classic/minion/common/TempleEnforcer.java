package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffDelta;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;

public class TempleEnforcer extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Give a friendly minion +3 Health
     */
    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    private final static EffectCharacter battlecryAction = new EffectCharacterBuffDelta(0, 3);

    public TempleEnforcer() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return TempleEnforcer.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return TempleEnforcer.battlecryAction;
    }
}
