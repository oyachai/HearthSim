package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;

public class ArgentProtector extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Give a friendly minion Divine Shield
     */
    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    private final static EffectCharacter battlecryAction = (targetSide, targetCharacterIndex, boardState) -> {
        Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        targetMinion.setDivineShield(true);
        return boardState;
    };

    public ArgentProtector() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return ArgentProtector.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return ArgentProtector.battlecryAction;
    }
}
