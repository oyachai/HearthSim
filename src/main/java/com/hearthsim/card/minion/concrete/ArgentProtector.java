package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;

public class ArgentProtector extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Give a friendly minion Divine Shield
     */
    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    private final static CardEffectCharacter battlecryAction = (originSide, origin, targetSide, targetCharacterIndex, boardState) -> {
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
    public CardEffectCharacter getBattlecryEffect() {
        return ArgentProtector.battlecryAction;
    }
}
