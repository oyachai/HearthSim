package com.hearthsim.card.classic.spell.epic;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.util.tree.CardDrawNode;

public class LayOnHands extends SpellTargetableCard {

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public LayOnHands() {
        super();
    }

    //Let's assume that it is never beneficial to heal an opponent... though this may not strictly be true under some very corner cases (e.g., with a Northshire Cleric)
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.FRIENDLY_MINIONS;
    }

    /**
     *
     * Use the card on the given target
     *
     * Restore 8 Health and draw 3 cards
     * @return The boardState is manipulated and returned
     */
    @Override
    public EffectCharacter getTargetableEffect() {
        if (this.effect == null) {
            this.effect = (targetSide, targetCharacterIndex, boardState) -> {
                Minion targetCharacter = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
                boardState = targetCharacter.takeHealAndNotify((byte) 8, targetSide, boardState);
                if (boardState instanceof CardDrawNode)
                    ((CardDrawNode) boardState).addNumCardsToDraw(3);
                else
                    boardState = new CardDrawNode(boardState, 3); //draw three cards

                return boardState;
            };
        }
        return this.effect;
    }
}
