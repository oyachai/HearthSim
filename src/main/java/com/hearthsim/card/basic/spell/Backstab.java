package com.hearthsim.card.basic.spell;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamageTargetableCard;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class Backstab extends SpellDamageTargetableCard {

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

            if (targetCharacter.getHealth() != targetCharacter.getMaxHealth()) {
                return false;
            }

            return true;
        }
    };

    public Backstab() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return Backstab.filter;
    }

    @Deprecated
    public Backstab(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }
}
