package com.hearthsim.card.classic.spell.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.model.PlayerSide;

public class InnerRage extends SpellTargetableCard {

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public InnerRage(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public InnerRage() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.ALL_MINIONS;
    }

    /**
     *
     * Use the card on the given target
     *
     * Deal 1 damage to a minion and give it +2 attack
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    public EffectCharacter getTargetableEffect() {
        if (this.effect == null) {
            this.effect = (targetSide, targetCharacterIndex, boardState) -> {
                Minion targetCharacter = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
                boardState = targetCharacter.takeDamageAndNotify((byte)1, PlayerSide.CURRENT_PLAYER, targetSide, boardState, true, false);
                targetCharacter.addAttack(2);
                return boardState;
            };
        }
        return this.effect;
    }
}
