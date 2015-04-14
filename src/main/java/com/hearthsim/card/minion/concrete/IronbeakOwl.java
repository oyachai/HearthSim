package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;

public class IronbeakOwl extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Silence a minion
     */
    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeEnemyMinions() {
            return true;
        }
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    private final static CardEffectCharacter battlecryAction = CardEffectCharacter.SILENCE;

    public IronbeakOwl() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return IronbeakOwl.filter;
    }

    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return IronbeakOwl.battlecryAction;
    }
}
