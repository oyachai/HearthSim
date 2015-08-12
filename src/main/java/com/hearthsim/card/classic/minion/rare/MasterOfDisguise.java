package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;

public class MasterOfDisguise extends Minion implements MinionBattlecryInterface {

    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    private final static EffectCharacter battlecryAction = (targetSide, targetCharacterIndex, boardState) -> {
        Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        targetMinion.setStealthedUntilRevealed(true);
        return boardState;
    };

    public MasterOfDisguise() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return MasterOfDisguise.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return MasterOfDisguise.battlecryAction;
    }
}
