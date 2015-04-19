package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffTemp;
import com.hearthsim.event.filter.FilterCharacterInterface;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;

public class AbusiveSergeant extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Give a minion +2 attack this turn
     */
    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeEnemyMinions() {
            return true;
        }
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    private final static EffectCharacter<Minion> battlecryAction = new EffectCharacterBuffTemp<>(2);

    public AbusiveSergeant() {
        super();
    }

    @Override
    public FilterCharacterInterface getBattlecryFilter() {
        return AbusiveSergeant.filter;
    }

    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return AbusiveSergeant.battlecryAction;
    }
}
