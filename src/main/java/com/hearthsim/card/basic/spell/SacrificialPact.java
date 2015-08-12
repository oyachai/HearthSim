package com.hearthsim.card.basic.spell;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.Minion.MinionTribe;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.model.PlayerSide;

public class SacrificialPact extends SpellTargetableCard {

    private final static FilterCharacter filter = new FilterCharacterTargetedSpell() {
        @Override
        protected boolean includeEnemyHero() {
            return true;
        }

        @Override
        protected boolean includeEnemyMinions() {
            return true;
        }

        @Override
        protected boolean includeOwnMinions() {
            return true;
        }

        @Override
        protected MinionTribe tribeFilter() {
            return MinionTribe.DEMON;
        }
    };

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public SacrificialPact() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return SacrificialPact.filter;
    }

    /**
     *
     * Use the card on the given target
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    public EffectCharacter getTargetableEffect() {
        if (this.effect == null) {
            this.effect = (targetSide, targetCharacterIndex, boardState) -> {
                Minion targetCharacter = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
                boardState = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getHero().takeHealAndNotify((byte) 5, PlayerSide.CURRENT_PLAYER, boardState);
                targetCharacter.setHealth((byte) -99);
                return boardState;
            };
        }
        return this.effect;
    }
}
