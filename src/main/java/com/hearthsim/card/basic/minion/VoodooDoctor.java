package com.hearthsim.card.basic.minion;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterHeal;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;

public class VoodooDoctor extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Restore 2 health
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

    private final static EffectCharacter battlecryAction = new EffectCharacterHeal(2);

    public VoodooDoctor() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return VoodooDoctor.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return VoodooDoctor.battlecryAction;
    }
}
