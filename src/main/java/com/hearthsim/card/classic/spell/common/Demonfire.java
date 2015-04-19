package com.hearthsim.card.classic.spell.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.Minion.MinionTribe;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;

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
     *
     * @param side
     * @param targetMinion The target minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    public EffectCharacter getTargetableEffect() {
        if (this.effect == null) {
            this.effect = (originSide, origin, targetSide, targetCharacterIndex, boardState) -> {
                Minion targetCharacter = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
                if (isCurrentPlayer(targetSide) && targetCharacter.getTribe() == MinionTribe.DEMON) {
                    targetCharacter.setAttack((byte)(targetCharacter.getAttack() + 2));
                    targetCharacter.setHealth((byte)(targetCharacter.getHealth() + 2));
                } else {
                    boardState = targetCharacter.takeDamageAndNotify((byte) 2, originSide, targetSide, boardState, true, false);
                }
                return boardState;
            };
        }
        return this.effect;
    }
}
