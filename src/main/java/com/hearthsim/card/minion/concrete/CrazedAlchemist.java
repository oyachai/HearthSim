package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;

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

    private final static CardEffectCharacter battlecryAction = (originSide, origin, targetSide, targetCharacterIndex, boardState) -> {
        Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        byte newHealth = targetMinion.getTotalAttack();
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
    public CardEffectCharacter getBattlecryEffect() {
        return CrazedAlchemist.battlecryAction;
    }
}
