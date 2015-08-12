package com.hearthsim.card.basic.spell;

import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.util.tree.CardDrawNode;

public class ShieldBlock extends SpellTargetableCard {


    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public ShieldBlock() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.SELF;
    }

    /**
     *
     * Use the card on the given target
     *
     * Gives all friendly characters +2 attack for this turn
     * @return The boardState is manipulated and returned
     */
    @Override
    public EffectCharacter getTargetableEffect() {
        if (this.effect == null) {
            this.effect = (targetSide, targetCharacterIndex, boardState) -> {
                boardState.data_.getCurrentPlayer().getHero().setArmor((byte)(boardState.data_.getCurrentPlayer().getHero().getArmor() + 5));
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
