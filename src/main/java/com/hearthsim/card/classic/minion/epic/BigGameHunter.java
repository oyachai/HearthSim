package com.hearthsim.card.classic.minion.epic;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;

public class BigGameHunter extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Destroy a minion with an Attack of 7 or more
     */
    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeEnemyMinions() {
            return true;
        }
        protected boolean includeOwnMinions() {
            return true;
        }

        @Override
        protected int minAttack() {
            return 7;
        }
    };

    private final static EffectCharacter battlecryAction = EffectCharacter.DESTROY;

    public BigGameHunter() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return BigGameHunter.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return BigGameHunter.battlecryAction;
    }
}
