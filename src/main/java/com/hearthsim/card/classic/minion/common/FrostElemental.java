package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;

public class FrostElemental extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Freeze a character
     */
    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeEnemyHero() {
            return true;
        }
        protected boolean includeEnemyMinions() {
            return true;
        }
//        protected boolean includeOwnHero() { return true; }
//        protected boolean includeOwnMinions() { return true; }
    };

    private final static EffectCharacter battlecryAction = EffectCharacter.FREEZE;

    public FrostElemental() {
        super();
    }

    /**
     * Let's assume that it is never a good idea to freeze your own character
     */
    @Override
    public FilterCharacter getBattlecryFilter() {
        return FrostElemental.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return FrostElemental.battlecryAction;
    }
}
