package com.hearthsim.card.basic.spell;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class HandOfProtection extends SpellTargetableCard {

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

            if (targetCharacter.getDivineShield()) {
                return false;
            }

            return true;
        }
    };

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public HandOfProtection() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return HandOfProtection.filter;
    }

    /**
     *
     * Use the card on the given target
     *
     * This card gives a minion Divine Shield.
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    public EffectCharacter getTargetableEffect() {
        if (this.effect == null) {
            this.effect = (targetSide, targetCharacterIndex, boardState) -> {
                Minion targetCharacter = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
                targetCharacter.setDivineShield(true);
                return boardState;
            };
        }
        return this.effect;
    }
}
