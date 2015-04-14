package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuffDelta;

public class TempleEnforcer extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Give a friendly minion +3 Health
     */
    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    private final static CardEffectCharacter battlecryAction = new CardEffectCharacterBuffDelta(0, 3);

    public TempleEnforcer() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return TempleEnforcer.filter;
    }

    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return TempleEnforcer.battlecryAction;
    }
}
