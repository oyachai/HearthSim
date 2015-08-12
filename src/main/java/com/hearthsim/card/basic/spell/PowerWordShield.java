package com.hearthsim.card.basic.spell;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.util.tree.CardDrawNode;

public class PowerWordShield extends SpellTargetableCard {

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public PowerWordShield() {
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
     * Gives a minion +2 health and draw a card
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    public EffectCharacter getTargetableEffect() {
        if (this.effect == null) {
            this.effect = (targetSide, targetCharacterIndex, boardState) -> {
                Minion targetCharacter = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
                targetCharacter.setHealth((byte)(targetCharacter.getHealth() + 2));
                targetCharacter.setMaxHealth((byte)(targetCharacter.getMaxHealth() + 2));

                if (boardState instanceof CardDrawNode) {
                    ((CardDrawNode) boardState).addNumCardsToDraw(1);
                } else {
                    boardState = new CardDrawNode(boardState, 1); //draw two cards
                }
                return boardState;
            };
        }
        return this.effect;
    }
}
