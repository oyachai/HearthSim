package com.hearthsim.card.basic.minion;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterDamage;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;

public class IronforgeRifleman extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Deal 1 damage to a chosen target
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

    private final static EffectCharacter battlecryAction = new EffectCharacterDamage(1);

    public IronforgeRifleman() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return IronforgeRifleman.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return IronforgeRifleman.battlecryAction;
    }
}
