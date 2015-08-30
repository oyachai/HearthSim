package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;

public class CrazedAlchemist extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Swap the Attack and Health of a minion
     */
    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeEnemyMinions() {
            return true;
        }
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    private final static EffectCharacter battlecryAction = (targetSide, targetCharacterIndex, boardState) -> {
        Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        byte newHealth = targetMinion.getTotalAttack(boardState, targetSide);
        byte newAttack = targetMinion.getTotalHealth();
        targetMinion.setAttack(newAttack);
        targetMinion.setHealth(newHealth);
        return boardState;
    };

    public CrazedAlchemist() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return CrazedAlchemist.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return CrazedAlchemist.battlecryAction;
    }
}
