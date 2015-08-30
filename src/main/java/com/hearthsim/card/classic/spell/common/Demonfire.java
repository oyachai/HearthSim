package com.hearthsim.card.classic.spell.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.Minion.MinionTribe;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.model.PlayerSide;

public class Demonfire extends SpellTargetableCard {

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public Demonfire(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public Demonfire() {
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
     * Deals 2 damage to a minion.  If it's a friendly Demon, give it +2/+2 instead.
     * @return The boardState is manipulated and returned
     */
    @Override
    public EffectCharacter getTargetableEffect() {
        if (this.effect == null) {
            this.effect = (targetSide, targetCharacterIndex, boardState) -> {
                Minion targetCharacter = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
                if (isCurrentPlayer(targetSide) && targetCharacter.getTribe() == MinionTribe.DEMON) {
                    targetCharacter.addAttack(2);
                    targetCharacter.addHealth(2);
                } else {
                    boardState = targetCharacter.takeDamageAndNotify((byte) 2, PlayerSide.CURRENT_PLAYER, targetSide, boardState, true, false);
                }
                return boardState;
            };
        }
        return this.effect;
    }
}
