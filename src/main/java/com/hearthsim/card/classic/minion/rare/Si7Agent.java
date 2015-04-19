package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterDamage;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class Si7Agent extends Minion implements MinionBattlecryInterface {

    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeEnemyHero() {
            return true;
        }
        protected boolean includeEnemyMinions() {
            return true;
        }
        protected boolean includeOwnHero() {
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

    private final static EffectCharacter battlecryAction = new EffectCharacterDamage(2);

    public Si7Agent() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return Si7Agent.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return Si7Agent.battlecryAction;
    }
}
