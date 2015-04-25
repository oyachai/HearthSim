package com.hearthsim.card.classic.minion.epic;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;

public class CabalShadowPriest extends Minion implements MinionBattlecryInterface {

    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeEnemyMinions() {
            return true;
        }

        @Override
        protected int maxAttack() {
            return 2;
        }
    };

    private final static EffectCharacter battlecryAction = EffectCharacter.MIND_CONTROL;

    public CabalShadowPriest() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return CabalShadowPriest.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return CabalShadowPriest.battlecryAction;
    }
}
