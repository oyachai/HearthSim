package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffTemp;

public class DarkIronDwarf extends Minion implements MinionBattlecryInterface {

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

    private final static EffectCharacter battlecryAction = new EffectCharacterBuffTemp(2);

    public DarkIronDwarf() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return DarkIronDwarf.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return DarkIronDwarf.battlecryAction;
    }
}
