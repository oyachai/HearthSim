package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class LayOnHands extends SpellCard {

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public LayOnHands() {
        super();
    }

    //Let's assume that it is never beneficial to heal an opponent... though this may not strictly be true under some very corner cases (e.g., with a Northshire Cleric)
    public CharacterFilter getTargetableFilter() {
        return CharacterFilterTargetedSpell.FRIENDLY_MINIONS;
    }

    /**
     *
     * Use the card on the given target
     *
     * Restore 8 Health and draw 3 cards
     *
     *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    public CardEffectCharacter getTargetableEffect() {
        if (this.effect == null) {
            this.effect = new CardEffectCharacter() {
                @Override
                public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
                    Minion targetCharacter = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
                    boardState = targetCharacter.takeHealAndNotify((byte) 8, targetSide, boardState);
                    if (boardState instanceof CardDrawNode)
                        ((CardDrawNode) boardState).addNumCardsToDraw(3);
                    else
                        boardState = new CardDrawNode(boardState, 3); //draw three cards

                    return boardState;
                }
            };
        }
        return this.effect;
    }
}
