package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuffDelta;

public class ShatteredSunCleric extends Minion implements MinionBattlecryInterface {
    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    private final static CardEffectCharacter battlecryAction = new CardEffectCharacterBuffDelta(1, 1);

    public ShatteredSunCleric() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return ShatteredSunCleric.filter;
    }

    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return ShatteredSunCleric.battlecryAction;
    }
}
