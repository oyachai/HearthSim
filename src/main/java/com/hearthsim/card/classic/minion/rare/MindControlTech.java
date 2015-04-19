package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectOnResolveRandomCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class MindControlTech extends Minion implements EffectOnResolveRandomCharacter {

    private static final EffectCharacter effect = EffectCharacter.MIND_CONTROL;

    private static final FilterCharacter filter = new FilterCharacter() {

        @Override
        protected boolean includeEnemyMinions() {
            return true;
        }

        @Override
        public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
            if (!super.targetMatches(originSide, origin, targetSide, targetCharacter, board)) {
                return false;
            }

            return board.modelForSide(targetSide).getNumMinions() >= 4;
        }
    };

    @Override
    public EffectCharacter getRandomTargetEffect() {
        return MindControlTech.effect;
    }

    @Override
    public FilterCharacter getRandomTargetFilter() {
        return MindControlTech.filter;
    }
}
