package com.hearthsim.card.classic.spell.common;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffDelta;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class Rampage extends SpellTargetableCard {

    private final static EffectCharacter effect = new EffectCharacterBuffDelta(3, 3);

    private final static FilterCharacter filter = new FilterCharacterTargetedSpell() {
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

            if (targetCharacter.getHealth() == targetCharacter.getMaxHealth()) {
                return false;
            }

            return true;
        }
    };

    public Rampage() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return Rampage.filter;
    }

    @Override
    public EffectCharacter getTargetableEffect() {
        return Rampage.effect;
    }
}
