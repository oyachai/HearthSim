package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterHeal;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;

public class EarthenRingFarseer extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Give a minion +2 attack this turn
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

    private final static EffectCharacter battlecryAction = new EffectCharacterHeal(3);


    public EarthenRingFarseer() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return EarthenRingFarseer.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return EarthenRingFarseer.battlecryAction;
    }
}
