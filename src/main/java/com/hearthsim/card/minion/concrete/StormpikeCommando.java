package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterDamage;

public class StormpikeCommando extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Deal 2 damage to a chosen target
     */
    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeEnemyHero() {
            return true;
        }
        protected boolean includeEnemyMinions() {
            return true;
        }
        protected boolean includeOwnHero() {
            return true;
        }
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    private final static EffectCharacter battlecryAction = new EffectCharacterDamage(2);

    public StormpikeCommando() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return StormpikeCommando.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return StormpikeCommando.battlecryAction;
    }
}
