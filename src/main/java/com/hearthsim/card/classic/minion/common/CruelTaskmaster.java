package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CruelTaskmaster extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Deal 1 damage to a minion and give it +2 Attack
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
        HearthTreeNode toRet = boardState;
        targetMinion.addAttack(2);
        toRet = targetMinion.takeDamageAndNotify((byte) 1, PlayerSide.CURRENT_PLAYER, targetSide, toRet, false, true);
        return toRet;
    };

    public CruelTaskmaster() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return CruelTaskmaster.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return CruelTaskmaster.battlecryAction;
    }
}
