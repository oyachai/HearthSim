package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class HandOfProtection extends SpellCard {

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public HandOfProtection(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public HandOfProtection() {
        super();

        this.characterFilter = CharacterFilterTargetedSpell.ALL_MINIONS;
    }

    @Override
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        if (!super.canBeUsedOn(playerSide, minion, boardModel)) {
            return false;
        }

        if (minion.getDivineShield()) {
            return false;
        }

        return true;
    }

    /**
     *
     * Use the card on the given target
     *
     * This card gives a minion Divine Shield.
     *
     *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    protected CardEffectCharacter getEffect() {
        if (this.effect == null) {
            this.effect = new CardEffectCharacter() {
                @Override
                public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
                    Minion targetCharacter = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
                    targetCharacter.setDivineShield(true);
                    return boardState;
                }
            };
        }
        return this.effect;
    }
}
