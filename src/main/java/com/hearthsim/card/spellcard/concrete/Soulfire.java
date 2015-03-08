package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.IdentityLinkedList;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;

public class Soulfire extends SpellDamage {

    public Soulfire() {
        super();
    }

    @Deprecated
    public Soulfire(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     *
     * Discards a random card
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    public HearthTreeNode useOn(
            PlayerSide side,
            Minion targetMinion,
            HearthTreeNode boardState,
            Deck deckPlayer0,
            Deck deckPlayer1,
            boolean singleRealizationOnly)
        throws HSException {
        HearthTreeNode toRet = null;

        PlayerSide.CURRENT_PLAYER.getPlayer(boardState).addNumCardsUsed((byte)1);
        if (singleRealizationOnly) {
            toRet = super.useOn(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
            PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
            IdentityLinkedList<Card> hand = currentPlayer.getHand();
            if (hand.size() > 0) {
                Card targetCard = hand.get((int)(Math.random() * hand.size()));
                toRet.data_.removeCard_hand(targetCard);
            }
        } else {
            PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
            PlayerModel targetPlayer = boardState.data_.modelForSide(side);
            int targetCharacterIndex = targetMinion instanceof Hero ? 0 : targetPlayer.getMinions().indexOf(targetMinion) + 1;
            int thisCardIndex = currentPlayer.getHand().indexOf(this);
            if (currentPlayer.getHand().size() == 1) {
                toRet = this.callSuperUseOn(side, targetMinion, boardState, deckPlayer0, deckPlayer1, false);
                toRet.data_.getCurrentPlayer().addNumCardsUsed((byte)-1);
            } else {
                toRet = new RandomEffectNode(boardState, new HearthAction(HearthAction.Verb.USE_CARD, PlayerSide.CURRENT_PLAYER, thisCardIndex, side, targetCharacterIndex));
                for (int indx = 0; indx < currentPlayer.getHand().size(); ++indx) {
                    if (indx != thisCardIndex) {
                        HearthTreeNode cNode = new HearthTreeNode(toRet.data_.deepCopy());
                        PlayerModel cNodePlayer = cNode.data_.getCurrentPlayer();
                        Minion cTargetMinion = cNode.data_.getCharacter(side, targetCharacterIndex);
                        Soulfire cCard = (Soulfire)cNodePlayer.getHand().get(thisCardIndex);
                        cNode = cCard.callSuperUseOn(side, cTargetMinion, cNode, deckPlayer0, deckPlayer1, false);
                        cNode.data_.getCurrentPlayer().addNumCardsUsed((byte)-1);
                        if (cNode != null) {
                            cNode.data_.removeCard_hand(cNodePlayer.getHand().get(indx < thisCardIndex ? indx : indx - 1));
                            toRet.addChild(cNode);
                        }
                    }
                }
            }
        }
        return toRet;
    }

    // TODO: find a better way to do this...
    private HearthTreeNode callSuperUseOn(
            PlayerSide side,
            Minion targetMinion,
            HearthTreeNode boardState,
            Deck deckPlayer0,
            Deck deckPlayer1,
            boolean singleRealizationOnly)
        throws HSException {
        return super.useOn(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
    }
}
