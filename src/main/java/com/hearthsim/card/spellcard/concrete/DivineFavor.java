package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class DivineFavor extends SpellCard {
    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public DivineFavor(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public DivineFavor() {
        super();

        this.characterFilter = CharacterFilterTargetedSpell.SELF;
    }

    @Override
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        if (!super.canBeUsedOn(playerSide, minion, boardModel)) {
            return false;
        }

        int numCardsToDraw = boardModel.modelForSide(PlayerSide.WAITING_PLAYER).getHand().size() - boardModel.modelForSide(PlayerSide.CURRENT_PLAYER).getHand().size() + 1;
        if (numCardsToDraw < 1) {
            return false;
        }

        return true;
    }

    /**
     *
     * Use the card on the given target
     *
     * Draw cards until you have as many in hand as your opponent
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
                    PlayerModel currentPlayer = boardState.data_.modelForSide(originSide);
                    PlayerModel waitingPlayer = boardState.data_.modelForSide(targetSide);

                    int numCardsToDraw = waitingPlayer.getHand().size() - currentPlayer.getHand().size() + 1;
                    if (numCardsToDraw < 1) {
                        return null;
                    }

                    if (boardState instanceof CardDrawNode) {
                        ((CardDrawNode) boardState).addNumCardsToDraw(numCardsToDraw);
                    } else {
                        boardState = new CardDrawNode(boardState, numCardsToDraw); //draw two cards
                    }
                    return boardState;
                }
            };
        }
        return this.effect;
    }
}
