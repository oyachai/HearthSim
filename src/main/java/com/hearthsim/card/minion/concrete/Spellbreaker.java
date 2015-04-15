package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;
import com.hearthsim.event.effect.EffectCharacter;

public class Spellbreaker extends Minion implements MinionBattlecryInterface {

    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeEnemyMinions() {
            return true;
        }
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    private final static EffectCharacter battlecryAction = EffectCharacter.SILENCE;

    public Spellbreaker() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return Spellbreaker.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return Spellbreaker.battlecryAction;
    }
}
