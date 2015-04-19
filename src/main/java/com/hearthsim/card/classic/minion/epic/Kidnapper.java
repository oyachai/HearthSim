package com.hearthsim.card.classic.minion.epic;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class Kidnapper extends Minion implements MinionBattlecryInterface {

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

            return board.modelForSide(originSide).isComboEnabled();
        }
    };

    private final static EffectCharacter effect = EffectCharacter.BOUNCE;

    public Kidnapper() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return Kidnapper.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return Kidnapper.effect;
    }
}
