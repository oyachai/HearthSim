package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

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
    public CharacterFilter getTargetableFilter() {
        return CharacterFilterTargetedSpell.SELF;
    }

    /**
     *
     * Use the card on the given target
     *
     * Gives all friendly characters +2 attack for this turn
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
                    boardState.data_.getCurrentPlayer().getHero().setArmor((byte)(boardState.data_.getCurrentPlayer().getHero().getArmor() + 5));
                    if (boardState instanceof CardDrawNode) {
                        ((CardDrawNode) boardState).addNumCardsToDraw(1);
                    } else {
                        boardState = new CardDrawNode(boardState, 1); //draw two cards
                    }
                    return boardState;
                }
            };
        }
        return this.effect;
    }
}
