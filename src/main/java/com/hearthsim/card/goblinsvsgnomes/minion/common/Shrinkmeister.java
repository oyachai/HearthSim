package com.hearthsim.card.goblinsvsgnomes.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffTemp;
import com.hearthsim.event.filter.FilterCharacterInterface;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;

public class Shrinkmeister extends Minion implements MinionBattlecryInterface {

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

    private final static EffectCharacter<Minion> battlecryAction = new EffectCharacterBuffTemp<>(-2);

    public Shrinkmeister() {
        super();
    }

    @Override
    public FilterCharacterInterface getBattlecryFilter() {
        return Shrinkmeister.filter;
    }

    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return Shrinkmeister.battlecryAction;
    }
}
