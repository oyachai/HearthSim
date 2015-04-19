package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuff;
import com.hearthsim.event.filter.FilterCharacterInterface;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;

public class AldorPeacekeeper extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Change an enemy minion's attack to 1
     */
    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeEnemyMinions() {
            return true;
        }
    };

    private final static EffectCharacter<Minion> battlecryAction = new EffectCharacterBuff<>(1, 0);

    public AldorPeacekeeper() {
        super();
    }

    @Override
    public FilterCharacterInterface getBattlecryFilter() {
        return AldorPeacekeeper.filter;
    }

    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return AldorPeacekeeper.battlecryAction;
    }
}
