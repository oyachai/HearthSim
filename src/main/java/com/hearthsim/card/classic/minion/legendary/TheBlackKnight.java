package com.hearthsim.card.classic.minion.legendary;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class TheBlackKnight extends Minion implements MinionBattlecryInterface {

    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeEnemyMinions() {
            return true;
        }

        @Override
        public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
            if (!super.targetMatches(originSide, origin, targetSide, targetCharacter, board)) {
                return false;
            }

            return targetCharacter.getTaunt();
        }
    };

    private final static EffectCharacter battlecryAction = EffectCharacter.DESTROY;

    public TheBlackKnight() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return TheBlackKnight.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return TheBlackKnight.battlecryAction;
    }
}
