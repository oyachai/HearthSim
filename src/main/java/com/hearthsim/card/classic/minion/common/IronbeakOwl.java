package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;

public class IronbeakOwl extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Silence a minion
     */
    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeEnemyMinions() {
            return true;
        }
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    private final static EffectCharacter battlecryAction = EffectCharacter.SILENCE;

    public IronbeakOwl() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return IronbeakOwl.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return IronbeakOwl.battlecryAction;
    }
}
