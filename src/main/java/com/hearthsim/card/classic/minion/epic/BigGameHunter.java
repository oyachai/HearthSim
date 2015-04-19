package com.hearthsim.card.classic.minion.epic;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class BigGameHunter extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Destroy a minion with an Attack of 7 or more
     */
    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeEnemyMinions() {
            return true;
        }
        protected boolean includeOwnMinions() {
            return true;
        }

        @Override
        public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
            if (!super.targetMatches(originSide, origin, targetSide, targetCharacter, board)) {
                return false;
            }

            return targetCharacter.getTotalAttack() >= 7;
        }
    };

    private final static EffectCharacter battlecryAction = EffectCharacter.DESTROY;

    public BigGameHunter() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return BigGameHunter.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return BigGameHunter.battlecryAction;
    }
}
