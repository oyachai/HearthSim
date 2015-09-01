package com.hearthsim.card.thegrandtournament.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffDelta;
import com.hearthsim.event.filter.FilterCharacterInterface;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;

public class LanceCarrier extends Minion implements MinionBattlecryInterface {

    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    private final static EffectCharacter<Minion> battlecryAction = new EffectCharacterBuffDelta<>(2, 0);;

    public LanceCarrier() {
        super();
    }

    @Override
    public FilterCharacterInterface getBattlecryFilter() {
        return LanceCarrier.filter;
    }

    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return LanceCarrier.battlecryAction;
    }
}
