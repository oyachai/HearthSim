package com.hearthsim.card.classic.minion.epic;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class CabalShadowPriest extends Minion implements MinionBattlecryInterface {

    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeEnemyMinions() {
            return true;
        }

        @Override
        public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
            if (!super.targetMatches(originSide, origin, targetSide, targetCharacter, board)) {
                return false;
            }

            return targetCharacter.getAttack() <= 2;
        }
    };

    private final static EffectCharacter battlecryAction = EffectCharacter.MIND_CONTROL;

    public CabalShadowPriest() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return CabalShadowPriest.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return CabalShadowPriest.battlecryAction;
    }
}
